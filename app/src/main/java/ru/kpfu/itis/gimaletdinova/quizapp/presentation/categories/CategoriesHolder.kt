package ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemCategoryBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories.model.Category

class CategoriesHolder(
    private val binding: ItemCategoryBinding,
    private val onItemClicked: ((Category) -> Unit)
) : RecyclerView.ViewHolder(binding.root) {

    private var item: Category? = null

    init {
        binding.run {
            root.setOnClickListener {
                item?.let(onItemClicked)
            }
            categoryBtn.setOnClickListener {
                item?.let(onItemClicked)
            }
        }
    }

    fun bindItem(item: Category) {
        this.item = item
        with(binding) {
            categoryBtn.text = item.name
            questionNumberTv.text = root.context.getString(
                R.string.level_number,
                item.levelsNumber,
                item.totalLevelsNumber
            )
        }
    }

}
