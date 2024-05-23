package ru.kpfu.itis.gimaletdinova.quizapp.presentation.game

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentPrelaunchBinding
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.CATEGORY_ID
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.LEVEL_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.MODE
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.PLAYERS_NAMES
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class PrelaunchFragment : Fragment(R.layout.fragment_prelaunch) {

    private val binding: FragmentPrelaunchBinding by viewBinding(
        FragmentPrelaunchBinding::bind
    )

    private val questionViewModel: QuestionViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        questionViewModel.clear()
        val mode = requireArguments().getSerializable(MODE) as Mode
        questionViewModel.setMode(mode)

        lifecycleScope.launch {
            questionViewModel.setPlayers(arguments?.getStringArrayList(PLAYERS_NAMES))
        }

        if (questionViewModel.mode == Mode.MULTIPLAYER) {
            questionViewModel.getCategoriesList()
        } else {
            val categoryId = requireArguments().getInt(CATEGORY_ID)
            val level = requireArguments().getInt(LEVEL_NUMBER)

            questionViewModel.getQuestions(categoryId, LevelDifficulty.get(level))
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

            lifecycleScope.launch {
                questionViewModel.errorsChannel.consumeEach {
                    Toast.makeText(context, getString(R.string.network_error_dialog_text), Toast.LENGTH_SHORT).show()
                }
            }

            playBtn.setOnClickListener {
                if (questionViewModel.mode == Mode.MULTIPLAYER) {
                    findNavController().navigate(
                        R.id.action_prelaunchFragment_to_categoryChoiceFragment
                    )
                } else {
                    findNavController().navigate(
                        R.id.action_prelaunchFragment_to_questionFragment,
                        bundleOf(
                            CATEGORY_ID to requireArguments().getInt(CATEGORY_ID),
                            LEVEL_NUMBER to requireArguments().getInt(LEVEL_NUMBER),
                        )
                    )
                }
                requireArguments().clear()
            }
        }
    }
}