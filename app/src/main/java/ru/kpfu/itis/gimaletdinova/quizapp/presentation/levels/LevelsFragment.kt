package ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentLevelsBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.decoration.SimpleHorizontalMarginDecoration
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.decoration.SimpleVerticalMarginDecoration
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Level
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants.EASY_LEVELS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants.MEDIUM_LEVELS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode
import ru.kpfu.itis.gimaletdinova.quizapp.util.getValueInPx
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class LevelsFragment : Fragment(R.layout.fragment_levels) {

    private val binding: FragmentLevelsBinding by viewBinding(
        FragmentLevelsBinding::bind
    )
    private val levelsViewModel: LevelsViewModel by viewModels()
    private val args by navArgs<LevelsFragmentArgs>()

    private var levelsAdapter: LevelsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.categoryTv.text = args.categoryName
        initRecyclerView()

        with(levelsViewModel) {

            getLevels(args.categoryId)

            loadingFlow.observe(this@LevelsFragment) { isLoad ->
                binding.progressBar.apply {
                    visibility = if (isLoad) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }

            levelsFlow.observe(this@LevelsFragment) {
                it?.let {
                    levelsAdapter?.setItems(it)
                }
            }

        }
    }

    private fun initRecyclerView() {
        binding.levelsRv.apply {
            levelsAdapter = LevelsAdapter(LevelsDiffUtilItemCallback(), ::onItemClicked)
            adapter = levelsAdapter

            val manager = GridLayoutManager(
                requireContext(),
                SPAN_COUNT,
                RecyclerView.VERTICAL,
                false
            )
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0 ||
                        position == EASY_LEVELS_NUMBER + 1 ||
                        position == EASY_LEVELS_NUMBER + MEDIUM_LEVELS_NUMBER + 2
                    ) {
                        SPAN_COUNT
                    } else {
                        1
                    }
                }
            }
            layoutManager = manager

            val verticalMarginDecoration = SimpleVerticalMarginDecoration(
                VERTICAL_MARGIN_VALUE.getValueInPx(resources.displayMetrics)
            )
            val horizontalMarginDecoration = SimpleHorizontalMarginDecoration(
                HORIZONTAL_MARGIN_VALUE.getValueInPx(resources.displayMetrics)
            )
            addItemDecoration(verticalMarginDecoration)
            addItemDecoration(horizontalMarginDecoration)
        }
    }

    private fun onItemClicked(level: Level) {
        if (level.number <= levelsViewModel.levelsNumber) {
            findNavController().navigate(
                LevelsFragmentDirections.actionLevelsFragmentToResultsViewFragment(
                    args.categoryId,
                    args.categoryName,
                    level.number
                )
            )
        } else {
            findNavController().navigate(
                LevelsFragmentDirections.actionLevelsFragmentToPrelaunchFragment(
                    categoryId = args.categoryId,
                    levelNumber = level.number,
                    mode = Mode.SINGLE,
                    playersNames = null
                )
            )
        }
    }

    companion object {
        private const val SPAN_COUNT = 5
        private const val VERTICAL_MARGIN_VALUE = 2
        private const val HORIZONTAL_MARGIN_VALUE = 8
    }

}
