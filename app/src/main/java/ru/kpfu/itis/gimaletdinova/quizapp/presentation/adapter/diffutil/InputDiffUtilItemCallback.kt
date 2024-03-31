package ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.model.InputModel

class InputDiffUtilItemCallback : DiffUtil.ItemCallback<InputModel>() {
    override fun areItemsTheSame(oldItem: InputModel, newItem: InputModel): Boolean {
        return oldItem.position == newItem.position
    }

    override fun areContentsTheSame(oldItem: InputModel, newItem: InputModel): Boolean {
        return oldItem.isCorrect == newItem.isCorrect && oldItem.text == newItem.text
    }

}