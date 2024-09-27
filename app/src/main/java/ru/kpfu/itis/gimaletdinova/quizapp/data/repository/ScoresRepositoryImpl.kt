package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import ru.kpfu.itis.gimaletdinova.quizapp.data.local.dao.ScoresDao
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.ScoreEntity
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.UserScores
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.ScoresRepository
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.UserRepository
import javax.inject.Inject

class ScoresRepositoryImpl @Inject constructor(
    private val scoresDao: ScoresDao,
    private val userRepository: UserRepository
) : ScoresRepository {

    override suspend fun saveScores(totalNumber: Int, userNumber: Int) {
        val userId = userRepository.getUserId()
        val score = scoresDao.getScore(userId)
        if (score != null) {
            score.userScoresNumber += userNumber
            score.totalScoresNumber += totalNumber
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

    override suspend fun getScores(): UserScores? {
        val userId = userRepository.getUserId()
        scoresDao.getScore(userId)?.let {
            return UserScores(it.userScoresNumber, it.totalScoresNumber)
        }
        return null
    }
}