package ru.kpfu.itis.gimaletdinova.quizapp.presentation.game

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentPrelaunchBinding
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.CATEGORY_ID
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.IS_MULTIPLAYER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.LEVEL_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.PLAYERS_NAMES
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.exception.LevelDifficultyNotFoundException
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class PrelaunchFragment : Fragment(R.layout.fragment_prelaunch) {

    private val binding: FragmentPrelaunchBinding by viewBinding(
        FragmentPrelaunchBinding::bind
    )

    private val questionViewModel: QuestionViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val isMultiplayer = requireArguments().getBoolean(IS_MULTIPLAYER)

        if (isMultiplayer) {
            val players = requireArguments().getStringArray(PLAYERS_NAMES)
        } else {
            val categoryId = requireArguments().getInt(CATEGORY_ID)
            val level = requireArguments().getInt(LEVEL_NUMBER)
            questionViewModel.getQuestions(categoryId, getLevelDifficulty(level))
        }

        with(binding) {

            questionViewModel.loadingFlow.observe(this@PrelaunchFragment) { isLoad ->
                binding.progressBar.apply {
                    visibility = if (isLoad) {
                        View.VISIBLE
                    } else {
                        playBtn.isEnabled = true
                        View.GONE
                    }
                }
            }

            playBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_prelaunchFragment_to_questionFragment,
                    bundleOf(
                        IS_MULTIPLAYER to isMultiplayer
                    )
                )

            }
        }
    }

    companion object {
        fun getLevelDifficulty(number: Int): LevelDifficulty {
            val minMediumLevel = Constants.MEDIUM_LEVELS_NUMBER + Constants.EASY_LEVELS_NUMBER
            val minHardLevel = minMediumLevel + Constants.HARD_LEVELS_NUMBER
            return when (number) {
                in 1..Constants.EASY_LEVELS_NUMBER -> LevelDifficulty.EASY
                in Constants.EASY_LEVELS_NUMBER..minMediumLevel -> LevelDifficulty.MEDIUM
                in minMediumLevel..minHardLevel -> LevelDifficulty.HARD
                else -> throw LevelDifficultyNotFoundException()
            }
        }
    }
}