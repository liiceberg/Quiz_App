package ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentCategoriesBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories.model.Category


@AndroidEntryPoint
class CategoriesFragment : BaseFragment(R.layout.fragment_categories) {

    private val binding: FragmentCategoriesBinding by viewBinding(
        FragmentCategoriesBinding::bind
    )
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private var categoriesAdapter: CategoriesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initRecyclerView()

        with(categoriesViewModel) {
            getCategories()

            loadingFlow.observe { isLoad ->
                binding.progressBar.apply {
                    visibility = if (isLoad) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }

            categoriesFlow.observe {
                it?.let {
                    categoriesAdapter?.setItems(it)
                }
            }

            errorsChannel.receiveAsFlow().observe {
                showError(it.message)
                binding.noCategoriesTv.visibility = View.VISIBLE
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