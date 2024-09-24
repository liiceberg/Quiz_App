package ru.kpfu.itis.gimaletdinova.quizapp.presentation.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentPrelaunchBinding
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe
import ru.kpfu.itis.gimaletdinova.quizapp.util.showErrorMessage

@AndroidEntryPoint
class PrelaunchFragment : Fragment(R.layout.fragment_prelaunch) {

    private val binding: FragmentPrelaunchBinding by viewBinding(
        FragmentPrelaunchBinding::bind
    )

    private val questionViewModel: QuestionViewModel by activityViewModels()

    private val args by navArgs<PrelaunchFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(questionViewModel) {
            clear()
            setMode(args.mode)
            setPlayers(args.playersNames)

            when (this.mode) {
                Mode.MULTIPLAYER -> {
                    getCategoriesList()
                }
                Mode.SINGLE -> {
                    val categoryId = args.categoryId
                    val level = args.levelNumber
                    getQuestions(categoryId, LevelDifficulty.get(level))
                }
                else -> {}
            }


            with(binding) {

                errorsChannel.receiveAsFlow().observe(this@PrelaunchFragment) {
                    activity?.showErrorMessage(it.message)
                    playBtn.isEnabled = false
                }

                loadingFlow.observe(this@PrelaunchFragment) { isLoad ->
                    progressBar.apply {
                        visibility = if (isLoad) {
                            playBtn.isEnabled = false
                            View.VISIBLE
                        } else {
                            playBtn.isEnabled = true
                            View.GONE
                        }
                    }
                }

                playBtn.setOnClickListener {
                    val action = when (questionViewModel.mode) {
                        Mode.SINGLE -> {
                            PrelaunchFragmentDirections.actionPrelaunchFragmentToQuestionFragment(
                                args.categoryId,
                                args.levelNumber
                            )
                        }

                        else -> {
                            PrelaunchFragmentDirections.actionPrelaunchFragmentToCategoryChoiceFragment()
                        }
                    }
                    findNavController().navigate(action)
                }
            }
        }
    }
}