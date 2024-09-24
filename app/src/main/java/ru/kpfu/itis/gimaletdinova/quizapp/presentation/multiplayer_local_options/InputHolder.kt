package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer_local_options

import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemInputBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer_local_options.model.InputModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.Validator
import ru.kpfu.itis.gimaletdinova.quizapp.util.hideKeyboard

class InputHolder(
    private val binding: ItemInputBinding,
    private val onTextChanged: ((InputModel) -> Unit),
    private val validate: (String) -> Validator.ValidationResult
) : RecyclerView.ViewHolder(binding.root) {

    private var item: InputModel? = null

    init {
        binding.usernameEt.run {
            addTextChangedListener {
                item?.let { input ->
                    val name = text.toString()
                    val validationResult = validate(name)
                    input.isCorrect = validationResult.isValid
                    if (input.isCorrect.not()) {
                        error = validationResult.error
                    }
                    input.text = name
                    onTextChanged(input)
                }

                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            rootView.hideKeyboard()
                            clearFocus()
                        }
                    }
                    true
                }
            }
        }
    }

    fun bindItem(item: InputModel) {
        this.item = item
        with(binding) {
            usernameEtLayout.hint =
                root.context.getString(R.string.player_input_hint, item.position)
        }
    }

}