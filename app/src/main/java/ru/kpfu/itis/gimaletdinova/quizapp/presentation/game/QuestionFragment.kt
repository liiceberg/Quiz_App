package ru.kpfu.itis.gimaletdinova.quizapp.presentation.game

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentQuestionBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.OnBackPressedDuringGameCallback
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants.MIN_CORRECT_ANSWERS_NUMBER_TO_WIN
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode
import ru.kpfu.itis.gimaletdinova.quizapp.util.getThemeColor

@AndroidEntryPoint
class QuestionFragment : BaseFragment(R.layout.fragment_question) {

    private val binding: FragmentQuestionBinding by viewBinding(
        FragmentQuestionBinding::bind
    )

    private val questionViewModel: QuestionViewModel by activityViewModels()
    private val args by navArgs<QuestionFragmentArgs>()
    private val categoryId by lazy { args.categoryId }
    private val levelNumber by lazy { args.levelNumber }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            OnBackPressedDuringGameCallback(::onExitGame)
        )

        binding.run {

            with(questionViewModel) {

                if (onPause.not()) {
                    updateTimer()
                } else {
                    onPause = false
                }

                if (mode != Mode.MULTIPLAYER) {
                    usernameTv.visibility = View.GONE
                }

                for (i in 0 until answersLl.childCount) {
                    (answersLl.getChildAt(i) as? Button)?.setOnClickListener {
                        for (item in answersLl.children) {
                            item.isEnabled = false
                        }
                        lifecycleScope.launch {
                            verifyAnswer(i)
                        }
                    }
                }


                questionsFlow.observe { q ->
                    q?.let {
                        usernameTv.text = getPlayer()
                        questionNumberTv.text =
                            getString(R.string.question_number, q.number, questionsListSize)
                        questionTv.text = q.question

                        for (i in 0 until answersLl.childCount) {

                            answersLl.getChildAt(i).apply {

                                setBackgroundColor(
                                    context.getThemeColor(
                                        com.google.android.material.R.attr.colorPrimary
                                    )
                                )
                                isEnabled = true

                                (this as? Button)?.text = q.answers[i]
                            }
                        }
                        Log.d("CORRECT ANSWER", q.correctAnswerPosition.toString())
                    }

                }

                timeFlow.observe {
                    timerPb.progress = it
                    if (it == 0) {
                        lifecycleScope.launch {
                            verifyAnswer(-1)
                        }
                    }
                }
            }
        }
    }

    private suspend fun verifyAnswer(userAnswerPosition: Int) {
        with(questionViewModel) {
            if (mode == Mode.SINGLE) saveUserAnswer(userAnswerPosition)

            val correctAnswerPosition = questionsFlow.value?.correctAnswerPosition
            setAnswerColor(userAnswerPosition, correctAnswerPosition)

            if (userAnswerPosition == correctAnswerPosition) {
                saveScores(binding.usernameTv.text.toString())
            }

            delay(1_000)
            updateTimer()
            if (timeFlow.value == -1) {
                if (isGameOver()) {
                    finishGame()
                } else {
                    findNavController().navigate(
                        QuestionFragmentDirections.actionQuestionFragmentToCategoryChoiceFragment()
                    )
                }
            }
        }
    }

    private fun setAnswerColor(userAnswerPosition: Int, correctAnswerPosition: Int?) {

        val correctAnswerColor = com.google.android.material.R.attr.colorPrimaryVariant
        val incorrectAnswerColor = com.google.android.material.R.attr.colorSecondaryVariant

        binding.answersLl.run {
            for (i in 0 until childCount) {
                if (i == correctAnswerPosition) {
                    getChildAt(i).setBackgroundColor(
                        context.getThemeColor(
                            correctAnswerColor
                        )
                    )
                } else if (i == userAnswerPosition) {
                    getChildAt(i).setBackgroundColor(
                        context.getThemeColor(
                            incorrectAnswerColor
                        )
                    )
                }
            }
        }


    }

    private fun finishGame() {
        with(questionViewModel) {
            if (mode == Mode.SINGLE && scores.values.first() >= MIN_CORRECT_ANSWERS_NUMBER_TO_WIN) {
                saveLevel(
                    categoryId = categoryId,
                    levelNumber = levelNumber
                )
            }
        }
        navigateToResults()
    }

    private fun navigateToResults() {
        with(questionViewModel) {

            val playersNames = mutableListOf<String>()
            val playersScores = mutableListOf<Int>()

            scores.toList().sortedByDescending { it.second }.map {
                playersNames.add(it.first)
                playersScores.add(it.second)
            }

            val action = when (mode) {
                Mode.SINGLE -> QuestionFragmentDirections.actionQuestionFragmentToResultsFragment(
                    mode,
                    playersNames = playersNames.toTypedArray(),
                    playersScores = playersScores.toIntArray(),
                    categoryId = categoryId,
                    levelNumber = levelNumber
                )

                Mode.MULTIPLAYER -> QuestionFragmentDirections.actionQuestionFragmentToResultsFragmentMultiplayer(
                    mode,
                    playersNames.toTypedArray(),
                    playersScores.toIntArray()
                )

                Mode.ONLINE -> QuestionFragmentDirections.actionQuestionFragmentToRoomFragment(
                    playersScores = playersScores.toIntArray()
                )
            }
            findNavController().navigate(
                action
            )
        }
    }

    private fun onExitGame() {
        when (questionViewModel.mode) {
            Mode.ONLINE -> navigateToResults()
            else -> findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        questionViewModel.onPause = true
        super.onDestroyView()
    }
}