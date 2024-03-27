package ru.kpfu.itis.gimaletdinova.quizapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemCategoryBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.holder.CategoriesHolder
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.model.Category

class CategoriesAdapter(
    private val items: MutableList<Category>,
    private val onItemClicked: ((Category) -> Unit)
) : RecyclerView.Adapter<CategoriesHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHolder =
        CategoriesHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoriesHolder, position: Int) {
        holder.bindItem(items[position])
    }

}