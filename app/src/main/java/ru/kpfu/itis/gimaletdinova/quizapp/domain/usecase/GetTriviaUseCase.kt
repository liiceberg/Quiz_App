package ru.kpfu.itis.gimaletdinova.quizapp.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.TriviaRepository
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.Category
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.QuestionType
import javax.inject.Inject

class GetTriviaUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: TriviaRepository
) {
    suspend operator fun invoke(
        amount: Int,
        category: Category,
        difficulty: LevelDifficulty,
        type: QuestionType
    ): QuestionsList {
        return withContext(dispatcher) {
            repository.getTrivia(amount, category.id, difficulty.toString(), type.toString())
        }
    }
}