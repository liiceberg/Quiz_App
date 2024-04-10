package ru.kpfu.itis.gimaletdinova.quizapp.domain.model

import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.Category
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.QuestionType

data class QuestionsList(
    val questions: List<QuestionModel>
)

data class QuestionModel(
    val number: Int,
    val type: QuestionType,
    val difficulty: LevelDifficulty,
    val category: Category,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)

fun QuestionsList.isEmptyResponse(): Boolean {
    for (q in questions) {
        if (q.question.isEmpty() || q.correctAnswer.isEmpty() || q.incorrectAnswers.isEmpty()) {
            return true
        }
    }
    return false
}
