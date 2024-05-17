package ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.QuestionEntity
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.LevelsRepository
import javax.inject.Inject

class LevelInteractor @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val levelRepository: LevelsRepository
) {
    suspend fun getNumberByCategory(id: Int): Int {
        return withContext(dispatcher) {
            levelRepository.getNumberByCategory(id)
        }
    }

    suspend fun saveQuestions(number: Int, categoryId: Int, questions: QuestionsList) {
        withContext(dispatcher) {
            levelRepository.saveQuestions(number, categoryId, questions)
        }
    }
    suspend fun getSavedQuestions(number: Int, categoryId: Int): List<QuestionEntity>? {
        return withContext(dispatcher) {
            levelRepository.getSavedQuestions(number, categoryId)
        }
    }
}