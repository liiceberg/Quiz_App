package ru.kpfu.itis.gimaletdinova.quizapp.domain.repository

interface LevelsRepository {
    suspend fun getNumberByCategory(id: Int): Int
}