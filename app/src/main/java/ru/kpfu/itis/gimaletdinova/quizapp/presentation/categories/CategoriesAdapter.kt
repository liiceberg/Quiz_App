package ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemCategoryBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories.model.Category

class CategoriesAdapter(
    diffCallback: DiffUtil.ItemCallback<Category>,
    private val onItemClicked: ((Category) -> Unit)
): ListAdapter<Category, CategoriesHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHolder =
        CategoriesHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )

    override fun onBindViewHolder(holder: CategoriesHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    fun setItems(items: List<Category>) {
        submitList(items)
    }

}