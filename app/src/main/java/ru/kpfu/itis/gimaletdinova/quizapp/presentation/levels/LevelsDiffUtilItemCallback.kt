package ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Item
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Level

class LevelsDiffUtilItemCallback: DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        if (oldItem is Level && newItem is Level) {
            return oldItem.number == newItem.number
        }
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        if (oldItem is Level && newItem is Level) {
            return oldItem.isBlocked == newItem.isBlocked
        }
        return oldItem == newItem
    }
}