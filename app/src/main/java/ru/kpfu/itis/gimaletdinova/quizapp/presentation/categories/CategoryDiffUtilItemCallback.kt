package ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories.model.Category

class CategoryDiffUtilItemCallback: DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.levelsNumber == newItem.levelsNumber
    }
}