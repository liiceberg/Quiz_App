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
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.MIN_CORRECT_ANSWERS_NUMBER_TO_WIN
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.CATEGORY_ID
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.LEVEL_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.MODE
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.PLAYERS_NAMES
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.PLAYERS_SCORES
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode
import ru.kpfu.itis.gimaletdinova.quizapp.util.OnBackPressed
import ru.kpfu.itis.gimaletdinova.quizapp.util.getThemeColor
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class QuestionFragment : Fragment(R.layout.fragment_question), OnBackPressed {
    override fun onBackPressed() {
        if (questionViewModel.mode == Mode.ONLINE) {
            navigateToResults()
        } else {
            activity?.onBackPressed()
        }
    }

    private val binding: FragmentQuestionBinding by viewBinding(
        FragmentQuestionBinding::bind
    )

    private val questionViewModel: QuestionViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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


                questionsFlow.observe(this@QuestionFragment) { q ->
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
                        println(q.correctAnswerPosition)
                    }

                }

                timeFlow.observe(this@QuestionFragment) {
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
                    findNavController().navigate(R.id.action_questionFragment_to_categoryChoiceFragment)
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
                    categoryId = requireArguments().getInt(CATEGORY_ID),
                    levelNumber = requireArguments().getInt(LEVEL_NUMBER)
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
                Mode.SINGLE -> R.id.action_questionFragment_to_resultsFragment
                Mode.MULTIPLAYER -> R.id.action_questionFragment_to_resultsFragment_multiplayer
                Mode.ONLINE -> R.id.action_questionFragment_to_roomFragment
            }
            findNavController().navigate(
                action,
                bundleOf(
                    MODE to mode,
                    PLAYERS_NAMES to playersNames,
                    PLAYERS_SCORES to playersScores,
                    CATEGORY_ID to arguments?.getInt(CATEGORY_ID),
                    LEVEL_NUMBER to arguments?.getInt(LEVEL_NUMBER),
                )
            )
        }
    }

    override fun onDestroyView() {
        questionViewModel.onPause = true
        super.onDestroyView()
    }
}