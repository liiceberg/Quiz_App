package ru.kpfu.itis.gimaletdinova.quizapp.presentation.results

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentResultsBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.results.model.ScoreModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.LEVELS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.MIN_CORRECT_ANSWERS_NUMBER_TO_WIN
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.IS_MULTIPLAYER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.PLAYERS_NAMES
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.PLAYERS_SCORES
import java.util.stream.Collectors

@AndroidEntryPoint
class ResultsFragment : Fragment(R.layout.fragment_results) {

    private val binding: FragmentResultsBinding by viewBinding(
        FragmentResultsBinding::bind
    )

    private val resultsViewModel: ResultsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val isMultiplayerMode = requireArguments().getBoolean(IS_MULTIPLAYER)

        val scores = getScoresList()

        with(binding) {
            if (isMultiplayerMode) {
                gameStatusTitleTv.visibility = View.GONE
                resultsTv.visibility = View.GONE
                imageIv.visibility = View.GONE

                nextBtn.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_resultsFragment_to_prelaunchFragment_multiplayer,
                        bundleOf(
                            IS_MULTIPLAYER to true,
                            PLAYERS_NAMES to scores.stream()
                                .map { model -> model.user }
                                .collect(Collectors.toList())
                        )
                    )
                }
            } else {

                val isWin = scores[0].score >= MIN_CORRECT_ANSWERS_NUMBER_TO_WIN

                val title = if (isWin) R.string.win_title_text else R.string.lose_title_text
                gameStatusTitleTv.text = getString(title)

                val text = if (isWin) R.string.win_text else R.string.lose_text
                resultsTv.text = getString(text)

                val img =
                    if (isWin) R.drawable.clap_hands_svgrepo_com else R.drawable.sad_svgrepo_com
                imageIv.setImageResource(img)

                resultsViewModel.saveScores(
                    scores[0].score,
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
                            IS_MULTIPLAYER to false,
                            Keys.CATEGORY_ID to requireArguments().getInt(Keys.CATEGORY_ID),
                            Keys.LEVEL_NUMBER to level
                        )
                    )
                }
            }
            lifecycleScope.launch {
                initRv(scores)
            }

            exitBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_resultsFragment_to_startFragment
                )
            }

        }
    }

    private fun initRv(scores: List<ScoreModel>) {
        binding.scoresRv.apply {
            adapter = ResultsAdapter(scores)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun getScoresList(): List<ScoreModel> {
        val list = mutableListOf<ScoreModel>()
        val players = requireArguments().getStringArrayList(PLAYERS_NAMES)
        val scores = requireArguments().getIntegerArrayList(PLAYERS_SCORES)
        if (players != null && scores != null) {
            for (i in players.indices) {
                list.add(ScoreModel(players[i], scores[i]))
            }
        }
        return list
    }
}