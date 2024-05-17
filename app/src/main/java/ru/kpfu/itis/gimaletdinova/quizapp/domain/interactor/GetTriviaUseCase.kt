package ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.QuestionType
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.TriviaRepository
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants
import javax.inject.Inject

class GetTriviaUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: TriviaRepository
) {
    suspend operator fun invoke(
        amount: Int = Constants.QUESTIONS_NUMBER,
        categoryId: Int,
        difficulty: LevelDifficulty,
        type: QuestionType = QuestionType.MULTIPLE_CHOICE
    ): QuestionsList {
        return withContext(dispatcher) {
            repository.getTrivia(amount, categoryId, difficulty, type)
        }
    }
}