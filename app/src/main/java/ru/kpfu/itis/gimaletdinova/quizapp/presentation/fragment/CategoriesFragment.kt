package ru.kpfu.itis.gimaletdinova.quizapp.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentCategoriesBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.CategoriesAdapter
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.model.Category
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.CATEGORY_NAME

class CategoriesFragment : BaseFragment(R.layout.fragment_categories) {

    private val binding: FragmentCategoriesBinding by viewBinding(
        FragmentCategoriesBinding::bind
    )
    private var categoriesAdapter: CategoriesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.levelsRv.apply {
//            TODO create categories list
            val c = Category("first", 2, 50)
            categoriesAdapter = CategoriesAdapter(mutableListOf(c, c, c, c), ::onItemClicked)
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun onItemClicked(category: Category) {
        binding.root.findNavController().navigate(
                R.id.action_categoriesFragment_to_levelsFragment,
                bundleOf(CATEGORY_NAME to category.name)
            )
    }

}