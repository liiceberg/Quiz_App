package ru.kpfu.itis.gimaletdinova.quizapp.domain.repository

import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.QuestionType

interface TriviaRepository {
    suspend fun getTrivia(
        amount: Int,
        category: Int,
        difficulty: LevelDifficulty,
        type: QuestionType
    ): QuestionsList

    suspend fun getCategories() : CategoriesList

}