package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import ru.kpfu.itis.gimaletdinova.quizapp.data.local.dao.QuestionsDao
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.LevelsRepository
import javax.inject.Inject

class LevelsRepositoryImpl @Inject constructor(
    private val questionsDao: QuestionsDao
) : LevelsRepository {
    override suspend fun getNumberByCategory(id: Int): Int {
        return questionsDao.getLevelsCountByCategory(id)
    }
}