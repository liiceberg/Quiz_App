package ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels


import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty.EASY
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty.HARD
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty.MEDIUM
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemLevelBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Level
import ru.kpfu.itis.gimaletdinova.quizapp.util.getThemeColor

class LevelsHolder(
    private val binding: ItemLevelBinding,
    private val onItemClicked: ((Level) -> Unit)
) : RecyclerView.ViewHolder(binding.root) {

    private var item: Level? = null

    init {
        binding.valueBtn.setOnClickListener {
            item?.let {
                if (it.isBlocked.not()) {
                    it.let(onItemClicked)
                }
            }
        }
    }

    fun bindItem(item: Level) {
        this.item = item
        with(binding) {

            valueBtn.text = item.number.toString()

            val backgroundColor = if (item.isBlocked) {
                com.google.android.material.R.attr.colorSecondary
            } else when (item.difficulty) {
                EASY -> com.google.android.material.R.attr.colorPrimaryVariant
                MEDIUM -> com.google.android.material.R.attr.colorPrimary
                HARD -> com.google.android.material.R.attr.colorSecondaryVariant
            }

            valueBtn.setBackgroundColor(
                root.context.getThemeColor(backgroundColor)
            )

            if (item.isBlocked) {
                valueBtn.isEnabled = false
            }
        }
    }
}