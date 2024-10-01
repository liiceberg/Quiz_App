package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.room

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentRoomBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.OnBackPressedDuringGameCallback
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode


@AndroidEntryPoint
class RoomFragment : BaseFragment(R.layout.fragment_room) {

    private val binding: FragmentRoomBinding by viewBinding(FragmentRoomBinding::bind)
    private val roomViewModel: RoomViewModel by activityViewModels()
    private val args by navArgs<RoomFragmentArgs>()

    private var messagesAdapter: MessagesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            OnBackPressedDuringGameCallback(::onExitRoom)
        )

        with(binding) {

            with(roomViewModel) {

                if (initialized.not()) {
                    args.roomCode?.let {
                        initStomp(it)
                    }
                }

                getPlayers()

                args.playersScores?.let {
                    readyBtn.visibility = View.GONE
                    sendScores(it[0])
                }

                initRv()

                readyBtn.setOnClickListener {
                    sendReadyMessage()
                    readyBtn.isEnabled = false
                }

                teamIv.setOnClickListener {
                    showPlayers()
                }

                messageFlow.observe {
                    messagesAdapter?.add(it)
                }

                unreadyPlayersNumberFlow.observe {
                    if (it == 0) {
                        resetUnreadyPlayersNumber()
                        findNavController().navigate(
                            RoomFragmentDirections.actionRoomFragmentToLoadingFragment(room)
                        )
                    }
                }
                notFinishedPlayersNumberFlow.observe {
                    if (it == 0) {
                        resetNotFinishedPlayersNumber()
                        findNavController().navigate(
                            RoomFragmentDirections.actionRoomFragmentToResultsFragment(
                                mode = Mode.ONLINE,
                                roomCode = room
                            )
                        )
                    }
                }

                remainingCapacityFlow.observe {
                    if (it < 0) {
                        readyBtn.isEnabled = false
                        roomViewModel.clear()
                    }
                }

                exitFlow.observe { exited ->
                    if (exited) {
                        clear()
                        findNavController().navigateUp()
                    }
                }

                errorsChannel.receiveAsFlow().observe {
                    showError(it.message)
                }
            }

        }

    }

    private fun initRv() {
        binding.messagesRv.apply {
            messagesAdapter = MessagesAdapter(
                roomViewModel.messages.toMutableList()
            )
            adapter = messagesAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun showPlayers() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.players_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogText = dialog.findViewById<View>(R.id.players_list_tv) as TextView
        dialogText.text =
            getString(R.string.players, roomViewModel.players?.let { buildToText(it) })
        dialog.show()
    }

    private fun buildToText(list: List<String>): String {
        val builder = StringBuilder()
        for (i in list.indices) {
            builder.append(list[i])
            if (i != list.lastIndex) {
                builder.append("\n")
            }
        }
        return builder.toString()
    }

    private fun onExitRoom() {
        if (roomViewModel.initialized) {
            roomViewModel.exit()
        } else {
            findNavController().navigateUp()
        }
    }

}