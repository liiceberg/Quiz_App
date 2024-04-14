package ru.kpfu.itis.gimaletdinova.quizapp.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.TriviaRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: TriviaRepository
) {
    suspend operator fun invoke(): CategoriesList {
        return withContext(dispatcher) {
            repository.getCategories()
        }
    }
}