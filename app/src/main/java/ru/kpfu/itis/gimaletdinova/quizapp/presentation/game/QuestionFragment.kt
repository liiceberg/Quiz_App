package ru.kpfu.itis.gimaletdinova.quizapp.presentation.game

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentQuestionBinding
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.QUESTIONS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.IS_MULTIPLAYER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.PLAYER_SCORES
import ru.kpfu.itis.gimaletdinova.quizapp.util.getThemeColor
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class QuestionFragment : Fragment(R.layout.fragment_question) {

    private val binding: FragmentQuestionBinding by viewBinding(
        FragmentQuestionBinding::bind
    )

    private val questionViewModel: QuestionViewModel by activityViewModels()

    private var correctAnswersNumber = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        questionViewModel.updateTimer()

        binding.run {

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

            with(questionViewModel) {
                questionsFlow.observe(this@QuestionFragment) { q ->
                    q?.let {
                        questionNumberTv.text =
                            getString(R.string.question_number, q.number, QUESTIONS_NUMBER)
                        questionTv.text = q.question

                        for (i in 0 until answersLl.childCount) {

                            answersLl.getChildAt(i).apply {

                                setBackgroundColor(
                                    requireActivity().getThemeColor(
                                        com.google.android.material.R.attr.colorPrimary
                                    )
                                )
                                isEnabled = true

                                (this as? Button)?.text = q.answers[i]
                            }
                        }
                    }

                }

                timeFlow.observe(this@QuestionFragment) {
                    timerPb.progress = it
                }
            }
        }
    }

    private suspend fun verifyAnswer(userAnswerPosition: Int) {
        val correctAnswerPosition = questionViewModel.questionsFlow.value?.correctAnswerPosition
        setAnswerColor(userAnswerPosition, correctAnswerPosition)

        if (userAnswerPosition == correctAnswerPosition) correctAnswersNumber++

        delay(1_000)

        questionViewModel.updateTimer()
        if (questionViewModel.timeFlow.value == -1) navigateToResults()
    }

    private fun setAnswerColor(userAnswerPosition: Int, correctAnswerPosition: Int?) {

        val correctAnswerColor = com.google.android.material.R.attr.colorPrimaryVariant
        val incorrectAnswerColor = com.google.android.material.R.attr.colorSecondaryVariant

        binding.answersLl.run {
            for (i in 0 until childCount) {
                if (i == correctAnswerPosition) {
                    getChildAt(i).setBackgroundColor(
                        requireActivity().getThemeColor(
                            correctAnswerColor
                        )
                    )
                } else if (i == userAnswerPosition) {
                    getChildAt(i).setBackgroundColor(
                        requireActivity().getThemeColor(
                            incorrectAnswerColor
                        )
                    )
                }
            }
        }


    }

    private fun navigateToResults() {
        val isMultiplayer = requireArguments().getBoolean(IS_MULTIPLAYER)
        val bundle = if (isMultiplayer) {
            bundleOf(IS_MULTIPLAYER to true)
        } else {
            bundleOf(
                IS_MULTIPLAYER to false,
                PLAYER_SCORES to correctAnswersNumber
            )
        }
        findNavController().navigate(
            R.id.action_questionFragment_to_resultsFragment,
            bundle
        )
    }


}