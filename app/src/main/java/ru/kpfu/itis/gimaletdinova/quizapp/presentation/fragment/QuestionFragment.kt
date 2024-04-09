package ru.kpfu.itis.gimaletdinova.quizapp.presentation.fragment

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentQuestionBinding
import androidx.fragment.app.Fragment
@AndroidEntryPoint
class QuestionFragment : Fragment(R.layout.fragment_question) {
    private val binding: FragmentQuestionBinding by viewBinding(
        FragmentQuestionBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

}