package ru.kpfu.itis.gimaletdinova.quizapp.domain.repository

import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.UserScores

interface ScoresRepository {
    suspend fun saveScores(totalNumber: Int, userNumber: Int)
    suspend fun getScores() : UserScores?
}