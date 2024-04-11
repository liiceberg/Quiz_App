package ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels


import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemLevelBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Level
import ru.kpfu.itis.gimaletdinova.quizapp.util.LevelDifficulty.*

class LevelsHolder(
    private val binding: ItemLevelBinding,
    private val onItemClicked: ((Level) -> Unit)
) : RecyclerView.ViewHolder(binding.root) {

    private var item: Level? = null

    init {
        binding.root.setOnClickListener {
            item?.let(onItemClicked)
        }
    }

    fun bindItem(item: Level) {
        this.item = item
        with(binding) {
            valueBtn.text = item.number.toString()
            val backgroundColor = if (item.isBlocked) R.color.grey
            else
                when (item.difficulty) {
                    EASY -> R.color.yellow_green
                    MEDIUM -> R.color.light_blue
                    HARD -> R.color.pumpkin
                }

            valueBtn.setBackgroundColor(root.resources.getColor(backgroundColor, root.context.theme))
            if (item.isBlocked) valueBtn.isEnabled = false
        }
    }
}