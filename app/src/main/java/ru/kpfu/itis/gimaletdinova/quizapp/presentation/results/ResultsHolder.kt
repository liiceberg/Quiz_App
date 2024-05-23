package ru.kpfu.itis.gimaletdinova.quizapp.presentation.results

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Score
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemScoreBinding

class ResultsHolder(private val binding: ItemScoreBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindItem(item: Score) {
        binding.userTv.text = item.username ?: "user"
        binding.scoreTv.text = item.value.toString()
    }
}