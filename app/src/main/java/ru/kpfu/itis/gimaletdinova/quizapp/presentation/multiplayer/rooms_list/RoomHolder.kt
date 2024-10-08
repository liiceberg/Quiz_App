package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.rooms_list

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty.EASY
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty.HARD
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty.MEDIUM
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemRoomBinding
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.util.getThemeColor

class RoomHolder(
    private val binding: ItemRoomBinding,
    private val onItemClicked: ((Room) -> Unit),
    private val categories: CategoriesList
) : RecyclerView.ViewHolder(binding.root) {

    private var item: Room? = null

    init {
        binding.run {
            root.setOnClickListener {
                item?.let(onItemClicked)
            }
        }
    }

    fun bindItem(item: Room) {
        this.item = item
        with(binding) {

            item.category?.let {
                categoryTv.text = getCategoryById(item.category)
            }

            codeTv.text = item.code

            val color = when (item.difficulty) {
                EASY -> com.google.android.material.R.attr.colorPrimaryVariant
                MEDIUM -> com.google.android.material.R.attr.colorPrimary
                HARD -> com.google.android.material.R.attr.colorSecondaryVariant
                else -> com.google.android.material.R.attr.colorSecondary
            }
            root.background.setTint(root.context.getThemeColor(color))
        }
    }

    private fun getCategoryById(id: Int): String {
        return categories.categoriesList.first { c -> c.id == id }.displayName
    }
}
