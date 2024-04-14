package ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentCategoriesBinding
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories.model.Category
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.CATEGORY_NAME
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe
import java.util.stream.Collectors

@AndroidEntryPoint
class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private val binding: FragmentCategoriesBinding by viewBinding(
        FragmentCategoriesBinding::bind
    )
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private var categoriesAdapter: CategoriesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(categoriesViewModel) {
            getCategories()

            loadingFlow.observe(this@CategoriesFragment) { isLoad ->
                binding.progressBar.apply {
                    visibility = if (isLoad) {
                        View.VISIBLE
                    } else {
                        categoriesList?.let {
                            initRecyclerView(it)
                        }
                        View.GONE
                    }
                }
            }
        }
    }

    private fun initRecyclerView(list: CategoriesList) {
        binding.levelsRv.apply {
//            TODO add levels number
            val c = list.categoriesList
                .stream()
                .map { c -> Category(id = c.id, name = c.displayName) }
                .collect(Collectors.toList())
            categoriesAdapter = CategoriesAdapter(c, ::onItemClicked)
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun onItemClicked(category: Category) {
        binding.root.findNavController().navigate(
            R.id.action_categoriesFragment_to_levelsFragment,
            bundleOf(
                CATEGORY_NAME to category.name
            )
        )
    }

}