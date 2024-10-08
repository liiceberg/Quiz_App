package ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemLevelDifficultyBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Difficulty

class DifficultyHolder(private val binding: ItemLevelDifficultyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: Difficulty) {
        binding.difficultyTv.text = item.name
    }

}