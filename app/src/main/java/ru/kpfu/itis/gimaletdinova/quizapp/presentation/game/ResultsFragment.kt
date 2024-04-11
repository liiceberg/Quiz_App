package ru.kpfu.itis.gimaletdinova.quizapp.presentation.game

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentResultsBinding
import androidx.fragment.app.Fragment
@AndroidEntryPoint
class ResultsFragment : Fragment(R.layout.fragment_results) {
    private val binding: FragmentResultsBinding by viewBinding(
        FragmentResultsBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
        }
    }
}