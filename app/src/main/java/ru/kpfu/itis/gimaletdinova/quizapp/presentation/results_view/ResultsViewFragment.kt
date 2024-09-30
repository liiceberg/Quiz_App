package ru.kpfu.itis.gimaletdinova.quizapp.presentation.results_view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.QuestionEntity
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentResultsViewBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.decoration.SimpleVerticalMarginDecoration
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
import ru.kpfu.itis.gimaletdinova.quizapp.util.getValueInPx

@AndroidEntryPoint
class ResultsViewFragment : BaseFragment(R.layout.fragment_results_view) {
    private val binding: FragmentResultsViewBinding by viewBinding(
        FragmentResultsViewBinding::bind
    )
    private val resultsViewViewModel: ResultsViewViewModel by viewModels()
    private val args by navArgs<ResultsViewFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(resultsViewViewModel) {
            val level = args.levelNumber
            val catId = args.categoryId
            val catName = args.categoryName

            with(binding) {
                categoryTv.text = catName
                levelTv.text = level.toString()
                backBtn.setOnClickListener {
                    findNavController().popBackStack()
                }
            }

            getQuestions(level, catId)

            loadingFlow.observe { isLoad ->
                binding.progressBar.apply {
                    visibility = if (isLoad) {
                        View.VISIBLE
                    } else {
                        savedQuestions?.let { initRv(it) }
                        View.GONE
                    }
                }
            }
        }
    }

    private fun initRv(list: List<QuestionEntity>) {
        binding.answersRv.apply {
            adapter = ResultsViewAdapter(list)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            val verticalMargin = SimpleVerticalMarginDecoration(
                VERTICAL_MARGIN_VALUE.getValueInPx(resources.displayMetrics)
            )
            addItemDecoration(verticalMargin)
        }
    }

    companion object {
        private const val VERTICAL_MARGIN_VALUE = 8
    }
}