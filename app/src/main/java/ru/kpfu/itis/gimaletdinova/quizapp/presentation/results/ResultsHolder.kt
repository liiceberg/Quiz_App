package ru.kpfu.itis.gimaletdinova.quizapp.presentation.results

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemScoreBinding
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.results.model.ScoreModel

class ResultsHolder(private val binding: ItemScoreBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindItem(item: ScoreModel) {
        binding.userTv.text = item.user
        binding.scoreTv.text = item.score.toString()
    }
}