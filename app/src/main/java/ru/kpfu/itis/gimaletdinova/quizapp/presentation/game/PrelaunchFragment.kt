package ru.kpfu.itis.gimaletdinova.quizapp.presentation.game

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
            val mode = args.mode
            setMode(mode)
            setPlayers(args.playersNames)

            if (this.mode == Mode.MULTIPLAYER) {
                getCategoriesList()
            } else {
                val categoryId = args.categoryId
                val level = args.levelNumber
                getQuestions(categoryId, LevelDifficulty.get(level))
            }


            with(binding) {
                errorsChannel.receiveAsFlow().observe(this@PrelaunchFragment) {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
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
                    if (questionViewModel.mode == Mode.MULTIPLAYER) {
                        findNavController().navigate(
                            R.id.action_prelaunchFragment_to_categoryChoiceFragment
                        )
                    } else {
                        findNavController().navigate(
                            PrelaunchFragmentDirections.actionPrelaunchFragmentToQuestionFragment(
                                args.categoryId,
                                args.levelNumber
                            )
                        )
                    }
                    requireArguments().clear()
                }
            }
        }
    }
}