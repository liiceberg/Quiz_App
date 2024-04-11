package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer_mode


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.FragmentCategoryChoiceBinding
import androidx.fragment.app.Fragment

@AndroidEntryPoint
class CategoryChoiceFragment : Fragment(R.layout.fragment_category_choice) {
    private val binding: FragmentCategoryChoiceBinding by viewBinding(
        FragmentCategoryChoiceBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            usernameTv.text = ""
            categoryChoiceSpinner.apply {
                ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.categories,
                    R.layout.spinner_category_item
                ).also { spinnerAdapter ->
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_category_item)
                    adapter = spinnerAdapter
                }

                onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (position != 0) {
                                root.findNavController()
                                    .navigate(
                                        R.id.action_categoryChoiceFragment_to_questionFragment,
                                        bundleOf()
                                    )
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
            }
        }
    }
}