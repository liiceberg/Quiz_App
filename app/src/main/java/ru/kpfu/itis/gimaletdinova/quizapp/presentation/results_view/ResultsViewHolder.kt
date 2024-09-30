package ru.kpfu.itis.gimaletdinova.quizapp.presentation.results_view

import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.QuestionEntity
import ru.kpfu.itis.gimaletdinova.quizapp.databinding.ItemQuestionBinding
import ru.kpfu.itis.gimaletdinova.quizapp.util.getThemeColor

class ResultsViewHolder(private val binding: ItemQuestionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: QuestionEntity) {
        with(binding) {
            questionTv.text = item.question
            for (i in 0 until answersLl.childCount) {

                answersLl.getChildAt(i).apply {

                    (this as? Button)?.text = item.answers[i]

                    val textColor = when (i) {
                        item.correctAnswerPosition -> {
                            com.google.android.material.R.attr.colorPrimaryVariant
                        }

                        item.userAnswerPosition -> {
                            com.google.android.material.R.attr.colorSecondaryVariant
                        }
                        else -> {
                            com.google.android.material.R.attr.colorSecondary
                        }
                    }

                    val buttonColor = when (i) {
                        item.userAnswerPosition -> R.color.light_grey
                        else -> R.color.white
                    }

                    (this as? Button)?.apply {
                        setTextColor(
                            root.context.getThemeColor(textColor)
                        )
                        setBackgroundColor(
                            root.resources.getColor(buttonColor, root.context.theme)
                        )
                    }
                }
            }
        }
    }
}