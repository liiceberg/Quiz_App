package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.UserNotFoundException
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.dao.QuestionsDao
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.LevelEntity
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.QuestionEntity
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.LevelsRepository
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys.USER_ID_KEY
import javax.inject.Inject

class LevelsRepositoryImpl @Inject constructor(
    private val questionsDao: QuestionsDao,
    private val dataStore: DataStore<Preferences>
) : LevelsRepository {
    override suspend fun getNumberByCategory(id: Int): Int {
        getUserId()?.let { userId ->
            return questionsDao.getLevelsCountByCategory(id, userId)
        }
        throw UserNotFoundException("User's id not found")
    }

    override suspend fun saveQuestions(number: Int, categoryId: Int, questions: QuestionsList) {
        getUserId()?.let { userId ->

            var levelId = questionsDao.getLevelId(categoryId, number, userId)
            if (levelId == null) {
                questionsDao.createLevel(
                    LevelEntity(
                        categoryId = categoryId,
                        number = number,
                        userId = userId
                    )
                )
                levelId = questionsDao.getLevelId(categoryId, number, userId)
            }

            for ((ind, q) in questions.questions.withIndex()) {
                questionsDao.saveQuestion(
                    QuestionEntity(
                        number = ind + 1,
                        question = q.question,
                        answers = q.answers,
                        correctAnswerPosition = q.correctAnswerPosition,
                        userAnswerPosition = q.userAnswerPosition,
                        levelId = levelId
                    )
                )
            }
        }
    }

    override suspend fun getSavedQuestions(number: Int, categoryId: Int): List<QuestionEntity>? {
        getUserId()?.let { userId ->
            val id = questionsDao.getLevelId(categoryId, number, userId)
            return questionsDao.getQuestions(id)
        }
        throw UserNotFoundException("User's id not found")
    }

    private suspend fun getUserId(): Long? {
        return dataStore.data.map {
            it[USER_ID_KEY]
        }.first()
    }
}