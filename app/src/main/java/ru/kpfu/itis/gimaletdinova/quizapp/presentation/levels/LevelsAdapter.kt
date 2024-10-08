package ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemLevelBinding
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemLevelDifficultyBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.exception.ViewTypeNotFoundException
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Difficulty
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Item
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Level

class LevelsAdapter(
    diffCallback: DiffUtil.ItemCallback<Item>,
    private val onItemClicked: ((Level) -> Unit)
) : ListAdapter<Item, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_level -> LevelsHolder(
                ItemLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemClicked
            )

            R.layout.item_level_difficulty -> DifficultyHolder(
                ItemLevelDifficultyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> throw ViewTypeNotFoundException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LevelsHolder -> holder.bindItem(getItem(position) as Level)
            is DifficultyHolder -> holder.bindItem(getItem(position) as Difficulty)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Level -> R.layout.item_level
            is Difficulty -> R.layout.item_level_difficulty
            else -> -1
        }
    }

    fun setItems(items: List<Item>) {
        submitList(items)
    }
}