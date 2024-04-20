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
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.MIN_CORRECT_ANSWERS_NUMBER_TO_WIN
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.IS_MULTIPLAYER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.PLAYER_SCORES

@AndroidEntryPoint
class ResultsFragment : Fragment(R.layout.fragment_results) {

    private val binding: FragmentResultsBinding by viewBinding(
        FragmentResultsBinding::bind
    )

    private val resultsViewModel: ResultsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val isMultiplayerMode = requireArguments().getBoolean(IS_MULTIPLAYER)

        with(binding) {
            if (isMultiplayerMode) {
                gameStatusTitleTv.visibility = View.GONE
                resultsTv.visibility = View.GONE
                imageIv.visibility = View.GONE
            } else {
                val scores = requireArguments().getInt(PLAYER_SCORES)
                val isWin = scores >= MIN_CORRECT_ANSWERS_NUMBER_TO_WIN

                val title = if (isWin) R.string.win_title_text else R.string.lose_title_text
                gameStatusTitleTv.text = getString(title)

                val text = if (isWin) R.string.win_text else R.string.lose_text
                resultsTv.text = getString(text)

                val img =
                    if (isWin) R.drawable.clap_hands_svgrepo_com else R.drawable.sad_svgrepo_com
                imageIv.setImageResource(img)

                resultsViewModel.saveScores(
                    scores,
                    Constants.QUESTIONS_NUMBER
                )
            }
            lifecycleScope.launch {
                initRv()
            }

            exitBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_resultsFragment_to_startFragment
                )
            }


            val bundle = if (isMultiplayerMode) {
                bundleOf(IS_MULTIPLAYER to true)
            } else {
                bundleOf(IS_MULTIPLAYER to false)
            }
            nextBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_resultsFragment_to_prelaunchFragment,
                    bundle
                )
            }
        }
    }

    private suspend fun initRv() {
        binding.scoresRv.apply {
            adapter = ResultsAdapter(getScoresList())
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private suspend fun getScoresList() : List<ScoreModel> {
        val list = mutableListOf<ScoreModel>()
        if (requireArguments().getBoolean(IS_MULTIPLAYER)) {
        } else {
            val user = resultsViewModel.getUsername()
            val score = requireArguments().getInt(PLAYER_SCORES)
            list.add(ScoreModel(user, score))
        }
        return list
    }
}