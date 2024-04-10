package ru.kpfu.itis.gimaletdinova.quizapp.domain.repository

import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList

interface TriviaRepository {
    suspend fun getTrivia(
        amount: Int,
        category: Int,
        difficulty: String,
        type: String
    ): QuestionsList
}