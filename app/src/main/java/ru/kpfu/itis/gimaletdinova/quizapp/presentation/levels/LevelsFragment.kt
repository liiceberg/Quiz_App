package ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentLevelsBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.decoration.SimpleHorizontalMarginDecoration
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.decoration.SimpleVerticalMarginDecoration
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Item
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Level
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.EASY_LEVELS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.MEDIUM_LEVELS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.CATEGORY_ID
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.CATEGORY_NAME
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.IS_MULTIPLAYER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.LEVEL_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.getValueInPx
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class LevelsFragment : Fragment(R.layout.fragment_levels) {
    private val binding: FragmentLevelsBinding by viewBinding(
        FragmentLevelsBinding::bind
    )

    private val levelsViewModel: LevelsViewModel by viewModels()

    private var levelsAdapter: LevelsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.categoryTv.text = requireArguments().getString(CATEGORY_NAME)

        with(levelsViewModel) {
            val catId = requireArguments().getInt(CATEGORY_ID)
            getLevels(catId)

            loadingFlow.observe(this@LevelsFragment) { isLoad ->
                binding.progressBar.apply {
                    visibility = if (isLoad) {
                        View.VISIBLE
                    } else {
                        initRecyclerView(levelsList)
                        View.GONE
                    }
                }
            }
        }
    }

    private fun initRecyclerView(list: List<Item>) {
        binding.levelsRv.apply {
            levelsAdapter = LevelsAdapter(list, ::onItemClicked)
            adapter = levelsAdapter
            val manager = GridLayoutManager(requireContext(), 5, RecyclerView.VERTICAL, false)
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0 ||
                        position == EASY_LEVELS_NUMBER + 1 ||
                        position == EASY_LEVELS_NUMBER + MEDIUM_LEVELS_NUMBER + 2
                    ) {
                        5
                    } else {
                        1
                    }
                }
            }
            layoutManager = manager

            val verticalMarginValue = 2.getValueInPx(resources.displayMetrics)
            val horizontalMarginValue = 8.getValueInPx(resources.displayMetrics)
            addItemDecoration(SimpleHorizontalMarginDecoration(horizontalMarginValue))
            addItemDecoration(SimpleVerticalMarginDecoration(verticalMarginValue))
        }
    }

    private fun onItemClicked(level: Level) {
        binding.root.findNavController().navigate(
            R.id.action_levelsFragment_to_prelaunchFragment,
            bundleOf(
                LEVEL_NUMBER to level.number,
                CATEGORY_ID to arguments?.getInt(CATEGORY_ID),
                IS_MULTIPLAYER to false
            )
        )
    }

}