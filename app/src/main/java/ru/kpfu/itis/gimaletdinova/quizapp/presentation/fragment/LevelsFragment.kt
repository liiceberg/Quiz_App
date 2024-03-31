package ru.kpfu.itis.gimaletdinova.quizapp.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentLevelsBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.LevelsAdapter
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.decoration.SimpleHorizontalMarginDecoration
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.decoration.SimpleVerticalMarginDecoration
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.model.Difficulty
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.model.Level
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.EASY_LEVELS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.MEDIUM_LEVELS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.CATEGORY_NAME
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.LEVEL_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.util.getValueInPx

class LevelsFragment : BaseFragment(R.layout.fragment_levels) {
    private val binding: FragmentLevelsBinding by viewBinding(
        FragmentLevelsBinding::bind
    )

    private var levelsAdapter: LevelsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        binding.categoryTv.text = arguments?.getString(CATEGORY_NAME)
    }

    private fun initRecyclerView() {
        binding.levelsRv.apply {
//            TODO create items list
            val level = Level(1, LevelDifficulty.EASY, false)
            val dif = Difficulty("Easy")
            val list =
                mutableListOf(dif, level, level, level, level, level, level, level, level)
            levelsAdapter = LevelsAdapter(list, ::onItemClicked)
            adapter = levelsAdapter
            val manager = GridLayoutManager(requireContext(), 5, RecyclerView.VERTICAL, false)
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0 ||
                        position == EASY_LEVELS_NUMBER + 1||
                        position == EASY_LEVELS_NUMBER + MEDIUM_LEVELS_NUMBER + 2
                    ) {
                        5
                    } else {
                        1
                    }
                }
            }
            layoutManager = manager

            val marginValue = 8.getValueInPx(resources.displayMetrics)
            addItemDecoration(SimpleHorizontalMarginDecoration(marginValue))
            addItemDecoration(SimpleVerticalMarginDecoration(marginValue))
        }
    }

    private fun onItemClicked(level: Level) {
        binding.root.findNavController().navigate(
            R.id.action_levelsFragment_to_prelaunchFragment,
            bundleOf(LEVEL_NUMBER to level.number)
        )
    }
}