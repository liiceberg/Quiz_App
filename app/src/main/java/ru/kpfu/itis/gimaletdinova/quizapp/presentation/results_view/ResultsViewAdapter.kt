package ru.kpfu.itis.gimaletdinova.quizapp.presentation.results_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.QuestionEntity
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemQuestionBinding

class ResultsViewAdapter(private val items: List<QuestionEntity>) :
    RecyclerView.Adapter<ResultsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        return ResultsViewHolder(
            ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size
}