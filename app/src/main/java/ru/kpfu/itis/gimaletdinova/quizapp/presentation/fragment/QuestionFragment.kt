package ru.kpfu.itis.gimaletdinova.quizapp.presentation.fragment

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentQuestionBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment

class QuestionFragment : BaseFragment(R.layout.fragment_question) {
    private val binding: FragmentQuestionBinding by viewBinding(
        FragmentQuestionBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

}