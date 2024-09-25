package ru.kpfu.itis.gimaletdinova.quizapp.presentation.game


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentCategoryChoiceBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.base.BaseFragment


@AndroidEntryPoint
class CategoryChoiceFragment : BaseFragment(R.layout.fragment_category_choice) {

    private val binding: FragmentCategoryChoiceBinding by viewBinding(
        FragmentCategoryChoiceBinding::bind
    )

    private val questionViewModel: QuestionViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var isQuestionsLoaded = false

        with(binding) {
            with(questionViewModel) {

                usernameTv.text = getPlayerToCategoryChoice()

                loadingFlow.observe { isLoad ->
                    progressBar.apply {
                        visibility = if (isLoad) {
                            View.VISIBLE
                        } else {
                            if (isQuestionsLoaded) {
                                onPause = false
                                findNavController().navigate(
                                    CategoryChoiceFragmentDirections.actionCategoryChoiceFragmentToQuestionFragment()
                                )
                            }
                            View.GONE
                        }
                    }
                }

                errorsChannel.receiveAsFlow().observe {
                    showError(it.message)
                }


                categoryChoiceSpinner.apply {

                    val list = mutableListOf<String>()
                    list.add("Select the category")
                    categoriesList?.let {
                        list.addAll(it.categoriesList.map { model -> model.displayName })
                    }

                    adapter = ArrayAdapter(requireContext(), R.layout.spinner_category_item, list)

                    onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position != 0) {
                                    categoriesList?.let {
                                        val catId = it.categoriesList[position - 1].id
                                        getQuestions(catId)
                                        isQuestionsLoaded = true
                                    }

                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                }
            }
        }

    }

}