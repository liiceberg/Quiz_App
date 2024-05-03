package ru.kpfu.itis.gimaletdinova.quizapp.domain.model

import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.QuestionType

data class QuestionsList(
    val questions: List<QuestionModel>
)

data class QuestionModel(
    val number: Int,
    val type: QuestionType,
    val difficulty: LevelDifficulty,
    val question: String,
    val answers: List<String>,
    val correctAnswerPosition: Int,
    var userAnswerPosition: Int = -1
)
