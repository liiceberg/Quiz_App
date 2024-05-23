package ru.kpfu.itis.gimaletdinova.quizapp.presentation.results

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Score
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemScoreBinding

class ResultsAdapter(private val items: List<Score>) :
    RecyclerView.Adapter<ResultsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsHolder {
        return ResultsHolder(
            ItemScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ResultsHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size
}