package ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.UserScores
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.ScoresRepository
import javax.inject.Inject

class ScoreInteractor @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val scoresRepository: ScoresRepository
) {
    suspend fun saveScores(totalNumber: Int, userNumber: Int) {
        withContext(dispatcher) {
            scoresRepository.saveScores(totalNumber, userNumber)
        }
    }
    suspend fun getScores() : UserScores? {
        return withContext(dispatcher) {
            scoresRepository.getScores()
        }
    }
}