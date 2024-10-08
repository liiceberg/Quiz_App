package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer_local_options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemInputBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer_local_options.model.InputModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.Validator

class InputAdapter(
    diffCallback: DiffUtil.ItemCallback<InputModel>,
    private val onTextChanged: ((InputModel) -> Unit),
    private val validate: (String) -> Validator.ValidationResult
): ListAdapter<InputModel, InputHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InputHolder {
        return InputHolder(
            ItemInputBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onTextChanged,
            validate
        )
    }
    override fun onBindViewHolder(holder: InputHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    fun setItems(items: List<InputModel>) {
        submitList(items)
    }
    fun updateItem(newItem: InputModel) {
        val list = currentList.toMutableList()
        list[newItem.position - 1] = newItem
        submitList(list)
    }
    fun removeItem(item: InputModel) {
        val list = currentList.toMutableList()
        list.remove(item)
        submitList(list)
    }

    fun addItem(item: InputModel) {
        val list = currentList.toMutableList()
        list.add(item)
        submitList(list)
    }

}