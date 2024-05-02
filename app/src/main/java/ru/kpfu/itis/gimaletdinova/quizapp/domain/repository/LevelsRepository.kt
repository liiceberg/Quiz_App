package ru.kpfu.itis.gimaletdinova.quizapp.domain.repository

import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.QuestionEntity
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList

interface LevelsRepository {
    suspend fun getNumberByCategory(id: Int): Int

    suspend fun saveQuestions(number: Int, categoryId: Int, questions: QuestionsList)
    suspend fun getSavedQuestions(number: Int, categoryId: Int): List<QuestionEntity>?
}