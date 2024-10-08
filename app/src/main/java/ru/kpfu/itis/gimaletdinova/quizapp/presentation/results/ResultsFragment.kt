package ru.kpfu.itis.gimaletdinova.quizapp.presentation.results

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Score
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentResultsBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.OnBackPressedDuringGameCallback
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants.LEVELS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants.MIN_CORRECT_ANSWERS_NUMBER_TO_WIN
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode


@AndroidEntryPoint
class ResultsFragment : BaseFragment(R.layout.fragment_results) {

    private val binding: FragmentResultsBinding by viewBinding(
        FragmentResultsBinding::bind
    )
    private val resultsViewModel: ResultsViewModel by viewModels()
    private val args by navArgs<ResultsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding) {
            when (args.mode) {
                Mode.MULTIPLAYER -> {
                    gameStatusTitleTv.visibility = View.GONE
                    resultsTv.visibility = View.GONE
                    imageIv.visibility = View.GONE

                    val scores = getScoresList()

                    nextBtn.setOnClickListener {
                        findNavController().navigate(
                            ResultsFragmentDirections.actionResultsFragmentToPrelaunchFragmentMultiplayer(
                                mode = Mode.MULTIPLAYER,
                                playersNames = scores.map { model -> model.username }.toTypedArray()
                            )
                        )
                    }

                    initRv(scores)

                    exitBtn.setOnClickListener {
                        findNavController().navigate(
                            ResultsFragmentDirections.actionResultsFragmentToStartFragment()
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
                        GameConfigConstants.QUESTIONS_NUMBER
                    )

                    var level = args.levelNumber

                    if (level == LEVELS_NUMBER) {
                        nextBtn.isEnabled = false
                    }
                    nextBtn.setOnClickListener {

                        if (isWin) ++level
                        findNavController().navigate(
                            ResultsFragmentDirections.actionResultsFragmentToPrelaunchFragment(
                                mode = Mode.SINGLE,
                                categoryId = args.categoryId,
                                levelNumber = level
                            )
                        )
                    }

                    initRv(scores)

                    exitBtn.setOnClickListener {
                        findNavController().navigate(
                            ResultsFragmentDirections.actionResultsFragmentToStartFragment()
                        )
                    }
                }

                Mode.ONLINE -> {
                    requireActivity().onBackPressedDispatcher.addCallback(
                        viewLifecycleOwner,
                        OnBackPressedDuringGameCallback {
                            findNavController().navigate(
                                ResultsFragmentDirections.actionResultsFragmentToRoomFragment()
                            )
                        }
                    )

                    gameStatusTitleTv.visibility = View.GONE
                    resultsTv.visibility = View.GONE
                    imageIv.visibility = View.GONE

                    with(resultsViewModel) {

                        args.roomCode?.let {
                            getResults(it)
                        }

                        resultsFlow.observe {
                            it?.let {
                                initRv(it)
                            }
                        }
                    }


                    nextBtn.setOnClickListener {
                        findNavController().navigate(
                            ResultsFragmentDirections.actionResultsFragmentToRoomFragment()
                        )
                    }

                    exitBtn.visibility = View.GONE

                    resultsViewModel.errorsChannel.receiveAsFlow().observe {
                        showError(it.message)
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
        val players = args.playersNames
        val scores = args.playersScores
        if (players != null && scores != null) {
            for (i in players.indices) {
                list.add(Score(players[i], scores[i]))
            }
        }
        return list
    }
}