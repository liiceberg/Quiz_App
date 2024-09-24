package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.room

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.Code
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.Message
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentRoomBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.game.QuestionViewModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode
import ru.kpfu.itis.gimaletdinova.quizapp.util.OnBackPressed
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe
import ru.kpfu.itis.gimaletdinova.quizapp.util.showErrorMessage


@AndroidEntryPoint
class RoomFragment : Fragment(R.layout.fragment_room), OnBackPressed {

    private val binding: FragmentRoomBinding by viewBinding(FragmentRoomBinding::bind)
    private val roomViewModel: RoomViewModel by activityViewModels()
    private val questionViewModel: QuestionViewModel by activityViewModels()
    private val args by navArgs<RoomFragmentArgs>()

    override fun onBackPressed() {
        roomViewModel.exit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding) {

            with(roomViewModel) {

                if (!initialized) {
                    initStomp(args.roomCode)
                }

                getPlayers()

                args.playersScores?.let {
                    readyBtn.visibility = View.GONE
                    sendMessage(
                        Message(
                            sender = userId,
                            code = Code.SCORE,
                            score = it[0]
                        )
                    )
                }

                if (messages.isNotEmpty()) {
                    infoTv.text = buildToText(messages)
                }

                readyBtn.setOnClickListener {
                    sendMessage(Message(userId, Code.READY))
                    readyBtn.isEnabled = false
                }

                teamIv.setOnClickListener {
                    showPlayers()
                }

                messageFlow.observe(this@RoomFragment) {
                    infoTv.text = buildToText(it)
                }

                waitFlow.observe(this@RoomFragment) {
                    if (it == 0) {
                        waitFlow.value = -1
                        with(questionViewModel) {
                            lifecycleScope.launch {
                                clear()
                                setMode(Mode.ONLINE)
                                getQuestions(roomViewModel.room)
                                setPlayers()
                                findNavController().navigate(
                                    RoomFragmentDirections.actionRoomFragmentToQuestionFragment()
                                )
                            }
                        }
                    }
                }
                resultsWaitFlow.observe(this@RoomFragment) {
                    if (it == 0) {
                        resultsWaitFlow.value = -1
                        findNavController().navigate(
                            RoomFragmentDirections.actionRoomFragmentToResultsFragment(
                                mode = Mode.ONLINE,
                                roomCode = roomViewModel.room
                            )
                        )
                    }
                }

                joinFlow.observe(this@RoomFragment) {
                    if (it < 0) {
                        readyBtn.isEnabled = false
                        roomViewModel.clear()
                    }
                }

                exitFlow.observe(this@RoomFragment) { exited ->
                    if (exited) {
                        clear()
                        findNavController().navigate(
                            RoomFragmentDirections.actionRoomFragmentToRoomsListContainerFragment()
                        )
                    }
                }

                errorsChannel.receiveAsFlow().observe(this@RoomFragment) {
                    activity?.showErrorMessage(it.message)
                }
            }

        }

        questionViewModel.loadingFlow.observe(this) { isLoad ->
            binding.progressBar.apply {
                visibility = if (isLoad) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
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

}