package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.AppException
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.QuestionType
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.makeSafeApiCall
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper.CategoriesMapper
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper.QuestionsListMapper
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.TriviaService
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.TriviaRepository
import javax.inject.Inject

class TriviaRepositoryImpl @Inject constructor(
    private val api: TriviaService,
    private val questionsListMapper: QuestionsListMapper,
    private val categoriesMapper: CategoriesMapper,
) : TriviaRepository {

    private var categoriesList: CategoriesList? = null

    override suspend fun getTrivia(
        amount: Int,
        category: Int,
        difficulty: LevelDifficulty,
        type: QuestionType
    ): QuestionsList {
        val domainModel = questionsListMapper.mapResponseToQuestionsList(
            makeSafeApiCall {
                api.getQuestions(amount, category, difficulty, type)
            }
        )
        return if (domainModel != null && domainModel.questions.isNotEmpty()) {
            domainModel
        } else {
            throw AppException.EmptyQuestionsListException("Questions not found")
        }
    }

    override suspend fun getCategories(): CategoriesList {
        if (categoriesList != null) {
            return categoriesList!!
        }
        val categories = categoriesMapper.mapResponseToCategoriesList(
            makeSafeApiCall {
                api.getCategories()
            }
        )
        if (categories != null && categories.categoriesList.isNotEmpty()) {
            categoriesList = categories
            return categoriesList!!
        } else {
            throw AppException.EmptyCategoriesListException("Categories not found")
        }
    }

}