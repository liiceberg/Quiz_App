package ru.kpfu.itis.gimaletdinova.quizapp.presentation.holder

import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemInputBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.model.InputModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.ValidationUtil

class InputHolder(
    private val binding: ItemInputBinding,
    private val onTextChanged: ((InputModel) -> Unit)
) : RecyclerView.ViewHolder(binding.root) {

    private var item: InputModel? = null

    init {
        binding.usernameEt.run {
            addTextChangedListener {
                item?.let { input ->
                    input.isCorrect = ValidationUtil.validateName(context, this)
                    input.text = text.toString()
                    onTextChanged(input)
                }
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        ValidationUtil.hideKeyboard(context, rootView)
                        clearFocus()
                    }
                }
                true
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