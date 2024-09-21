package ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentCategoriesBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories.model.Category
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private val binding: FragmentCategoriesBinding by viewBinding(
        FragmentCategoriesBinding::bind
    )
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private var categoriesAdapter: CategoriesAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        with(categoriesViewModel) {
            getCategories()

            loadingFlow.observe(this@CategoriesFragment) { isLoad ->
                binding.progressBar.apply {
                    visibility = if (isLoad) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }

            categoriesFlow.observe(this@CategoriesFragment) {
                it?.let {
                    categoriesAdapter?.setItems(it)
                }
            }

            errorsChannel.receiveAsFlow().observe(this@CategoriesFragment) {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initRecyclerView() {
        binding.levelsRv.apply {
            categoriesAdapter = CategoriesAdapter(CategoryDiffUtilItemCallback(), ::onItemClicked)
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun onItemClicked(category: Category) {
        findNavController().navigate(
            CategoriesFragmentDirections.actionCategoriesFragmentToLevelsFragment(
                category.id,
                category.name
            )
        )
    }

}