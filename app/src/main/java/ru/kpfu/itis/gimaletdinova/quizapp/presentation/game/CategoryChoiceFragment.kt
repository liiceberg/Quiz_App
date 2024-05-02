package ru.kpfu.itis.gimaletdinova.quizapp.presentation.game


import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentCategoryChoiceBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe

@AndroidEntryPoint
class CategoryChoiceFragment : Fragment(R.layout.fragment_category_choice) {

    private val binding: FragmentCategoryChoiceBinding by viewBinding(
        FragmentCategoryChoiceBinding::bind
    )

    private val questionViewModel: QuestionViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var isQuestionsLoaded = false

        with(binding) {
            usernameTv.text = questionViewModel.getPlayerToCategoryChoice()

            questionViewModel.loadingFlow.observe(this@CategoryChoiceFragment) { isLoad ->
                binding.progressBar.apply {
                    visibility = if (isLoad) {
                        View.VISIBLE
                    } else {
                        if (isQuestionsLoaded) {
                            questionViewModel.onPause = false
                            findNavController()
                                .navigate(R.id.action_categoryChoiceFragment_to_questionFragment)
                        }
                        View.GONE
                    }
                }
            }

            lifecycleScope.launch {
                questionViewModel.errorsChannel.consumeEach {
                    AlertDialog.Builder(context)
                        .setTitle(getString(R.string.unknown_error))
                        .setMessage(getString(R.string.network_error_dialog_text))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                            dialog.cancel()
                            findNavController().popBackStack()
                        }
                        .show()
                }
            }


            categoryChoiceSpinner.apply {

                val list = mutableListOf<String>()
                list.add("Select the category")
                questionViewModel.categoriesList?.let {
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
                                questionViewModel.categoriesList?.let {
                                    val catId = it.categoriesList[position - 1].id
                                    questionViewModel.getQuestions(catId)
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