package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.Code
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.MessageDto
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentRoomBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.game.QuestionViewModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.MODE
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.PLAYERS_SCORES
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.ROOM_CODE
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode
import ru.kpfu.itis.gimaletdinova.quizapp.util.OnBackPressed
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe


@AndroidEntryPoint
class RoomFragment : Fragment(R.layout.fragment_room), OnBackPressed {
    private val binding: FragmentRoomBinding by viewBinding(FragmentRoomBinding::bind)
    private val roomViewModel: RoomViewModel by activityViewModels()
    private val questionViewModel: QuestionViewModel by activityViewModels()

    override fun onBackPressed() {
        roomViewModel.clear()
        findNavController().navigate(R.id.action_roomFragment_to_roomsListFragment)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val room = arguments?.getString(ROOM_CODE)

        with(roomViewModel) {

            if (!initialized) {
                initStomp(room)
            }

            with(binding) {

                val scores = arguments?.getIntegerArrayList(PLAYERS_SCORES)

                if (scores != null) {
                    readyBtn.visibility = View.GONE
                    roomViewModel.sendMessage(MessageDto(sender = userId, code = Code.SCORE, score = scores[0]))
                }

                if (messages.isNotEmpty()) {
                    infoTv.text = getMessages(messages)
                }

                readyBtn.setOnClickListener {
                    sendMessage(MessageDto(userId, Code.READY))
                    readyBtn.isEnabled = false
                }
                messageFlow.observe(this@RoomFragment) {
                    infoTv.text = getMessages(it)
                }

                waitFlow.observe(this@RoomFragment) {
                    if (it == 0) {
                        waitFlow.value = -1
                        lifecycleScope.launch {
                            questionViewModel.clear()
                            questionViewModel.setMode(Mode.ONLINE)
                            questionViewModel.getQuestions(roomViewModel.room!!)
                            findNavController().navigate(R.id.action_roomFragment_to_questionFragment)
                        }
                    }
                }
                resultsWaitFlow.observe(this@RoomFragment) {
                    if (it == 0) {
                        resultsWaitFlow.value = -1
                        findNavController().navigate(R.id.action_roomFragment_to_resultsFragment,
                            bundleOf(
                                MODE to Mode.ONLINE,
                                ROOM_CODE to roomViewModel.room
                            )
                        )
                    }
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

    private fun getMessages(list: List<String>): String {
        val builder: StringBuilder = java.lang.StringBuilder()
        for (str in list) {
            builder.append(str)
            builder.append("\n")
        }
        return builder.toString()
    }

}