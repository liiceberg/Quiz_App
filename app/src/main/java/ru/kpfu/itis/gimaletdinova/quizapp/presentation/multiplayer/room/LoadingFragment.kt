package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.room

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.game.QuestionViewModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode

class LoadingFragment : BaseFragment(R.layout.fragment_loading) {

    private val questionViewModel: QuestionViewModel by activityViewModels()
    private val args by navArgs<LoadingFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(questionViewModel) {
            clear()
            setMode(Mode.ONLINE)
            getQuestions(args.room)
            setPlayers()

            loadingFlow.observe { isLoad ->
                if (isLoad.not() && questionsListSize != null) {
                    findNavController().navigate(
                        LoadingFragmentDirections.actionLoadingFragmentToQuestionFragment()
                    )
                }
            }

            errorsChannel.receiveAsFlow().observe {
                showError(it.message)
            }

        }
    }
}