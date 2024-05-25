package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.UserNotFoundException
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.dao.ScoresDao
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.ScoreEntity
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.UserScores
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.ScoresRepository
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys
import javax.inject.Inject

class ScoresRepositoryImpl @Inject constructor(
    private val scoresDao: ScoresDao,
    private val dataStore: DataStore<Preferences>
) : ScoresRepository {
    override suspend fun saveScores(totalNumber: Int, userNumber: Int) {
        getUserId()?.let { userId ->
            val score = scoresDao.getScore(userId)
            if (score != null) {
                score.userScoresNumber = score.userScoresNumber + userNumber
                score.totalScoresNumber = score.totalScoresNumber + totalNumber
                scoresDao.saveScore(score)
            } else {
                scoresDao.saveScore(
                    ScoreEntity(
                        userId = userId,
                        userScoresNumber = userNumber,
                        totalScoresNumber = totalNumber
                    )
                )
            }
        }
    }

    override suspend fun getScores(): UserScores? {
        getUserId()?.let { userId ->
            scoresDao.getScore(userId)?.let {
                return UserScores(it.userScoresNumber, it.totalScoresNumber)
            }
            return null
        }
        throw UserNotFoundException("User's id not found")
    }

    private suspend fun getUserId(): Long? {
        return dataStore.data.map {
            it[PrefsKeys.USER_ID_KEY]
        }.first()
    }
}