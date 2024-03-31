package ru.kpfu.itis.gimaletdinova.quizapp.presentation.fragment

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentResultsViewBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
@AndroidEntryPoint
class ResultsViewFragment : BaseFragment(R.layout.fragment_results_view) {
    private val binding: FragmentResultsViewBinding by viewBinding(
        FragmentResultsViewBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
        }
    }
}