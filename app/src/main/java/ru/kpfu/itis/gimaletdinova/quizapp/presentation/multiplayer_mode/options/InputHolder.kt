package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer_mode.options

import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemInputBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer_mode.options.model.InputModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.ValidationUtil
import ru.kpfu.itis.gimaletdinova.quizapp.util.hideKeyboard

class InputHolder(
    private val binding: ItemInputBinding,
    private val onTextChanged: ((InputModel) -> Unit)
) : RecyclerView.ViewHolder(binding.root) {

    private var item: InputModel? = null

    init {
        binding.usernameEt.run {
            addTextChangedListener {
                item?.let { input ->
                    val name = text.toString()
                    input.isCorrect = ValidationUtil.validateName(name)
                    if (input.isCorrect.not()) {
                        if (name.trim().isEmpty()) {
                            error = context.getString(R.string.empty_username_error)
                        } else if (name.matches(Regex("[A-Za-z]+")).not()) {
                            error = context.getString(R.string.incorrect_username_error)
                        }

                    }
                    input.text = name
                    onTextChanged(input)
                }

                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            hideKeyboard(context, rootView)
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