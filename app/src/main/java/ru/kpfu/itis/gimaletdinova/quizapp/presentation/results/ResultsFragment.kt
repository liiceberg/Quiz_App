package ru.kpfu.itis.gimaletdinova.quizapp.presentation.results

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Score
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentResultsBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.room.RoomViewModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.LEVELS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.MIN_CORRECT_ANSWERS_NUMBER_TO_WIN
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.MODE
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.PLAYERS_NAMES
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.PLAYERS_SCORES
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.ROOM_CODE
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe
import java.util.stream.Collectors

@AndroidEntryPoint
class ResultsFragment : Fragment(R.layout.fragment_results) {

    private val binding: FragmentResultsBinding by viewBinding(
        FragmentResultsBinding::bind
    )

    private val resultsViewModel: ResultsViewModel by viewModels()
    private val roomViewModel: RoomViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val mode = requireArguments().getSerializable(MODE) as Mode

        with(binding) {
            when (mode) {
                Mode.MULTIPLAYER -> {
                    gameStatusTitleTv.visibility = View.GONE
                    resultsTv.visibility = View.GONE
                    imageIv.visibility = View.GONE

                    val scores = getScoresList()

                    nextBtn.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_resultsFragment_to_prelaunchFragment_multiplayer,
                            bundleOf(
                                MODE to Mode.MULTIPLAYER,
                                PLAYERS_NAMES to scores.stream()
                                    .map { model -> model.username }
                                    .collect(Collectors.toList())
                            )
                        )
                    }

                    initRv(scores)

                    exitBtn.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_resultsFragment_to_startFragment
                        )
                    }
                }

                Mode.SINGLE -> {

                    val scores = getScoresList()

                    val isWin = scores[0].value >= MIN_CORRECT_ANSWERS_NUMBER_TO_WIN

                    val title = if (isWin) R.string.win_title_text else R.string.lose_title_text
                    gameStatusTitleTv.text = getString(title)

                    val text = if (isWin) R.string.win_text else R.string.lose_text
                    resultsTv.text = getString(text)

                    val img =
                        if (isWin) R.drawable.clap_hands_svgrepo_com else R.drawable.sad_svgrepo_com
                    imageIv.setImageResource(img)

                    resultsViewModel.saveScores(
                        scores[0].value,
                        Constants.QUESTIONS_NUMBER
                    )

                    var level = requireArguments().getInt(Keys.LEVEL_NUMBER)

                    if (level == LEVELS_NUMBER) {
                        nextBtn.isEnabled = false
                    }
                    nextBtn.setOnClickListener {

                        if (isWin) ++level

                        findNavController().navigate(
                            R.id.action_resultsFragment_to_prelaunchFragment,
                            bundleOf(
                                MODE to Mode.SINGLE,
                                Keys.CATEGORY_ID to requireArguments().getInt(Keys.CATEGORY_ID),
                                Keys.LEVEL_NUMBER to level
                            )
                        )
                    }

                    initRv(scores)

                    exitBtn.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_resultsFragment_to_startFragment
                        )
                    }
                }

                Mode.ONLINE -> {
                    gameStatusTitleTv.visibility = View.GONE
                    resultsTv.visibility = View.GONE
                    imageIv.visibility = View.GONE

                    with(resultsViewModel) {

                        val room = requireArguments().getString(ROOM_CODE)
                        room?.let {
                            getResults(room)
                        }

                        resultsFlow.observe(this@ResultsFragment) {
                            it?.let {
                                initRv(it)
                            }
                        }
                    }


                    nextBtn.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_resultsFragment_to_roomFragment
                        )
                    }

                    exitBtn.setOnClickListener {
                        roomViewModel.exit()
                    }

                    roomViewModel.exitFlow.observe(this@ResultsFragment) { exited ->
                        if (exited) {
                            roomViewModel.clear()
                            findNavController().navigate(
                                R.id.action_resultsFragment_to_startFragment
                            )
                        }
                    }

                    resultsViewModel.errorsChannel.receiveAsFlow().observe(this@ResultsFragment) {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }

    private fun initRv(scores: List<Score>) {
        binding.scoresRv.apply {
            adapter = ResultsAdapter(scores)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun getScoresList(): List<Score> {
        val list = mutableListOf<Score>()
        val players = requireArguments().getStringArrayList(PLAYERS_NAMES)
        val scores = requireArguments().getIntegerArrayList(PLAYERS_SCORES)
        if (players != null && scores != null) {
            for (i in players.indices) {
                list.add(Score(players[i], scores[i]))
            }
        }
        return list
    }
}