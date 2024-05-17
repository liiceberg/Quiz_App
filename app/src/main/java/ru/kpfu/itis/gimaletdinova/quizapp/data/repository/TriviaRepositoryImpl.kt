package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.EmptyCategoriesListException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.EmptyQuestionsListException
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper.CategoriesMapper
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper.QuestionsListMapper
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.TriviaRepository
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.QuestionType
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.TriviaService
import javax.inject.Inject

class TriviaRepositoryImpl @Inject constructor(
    private val api: TriviaService,
    @ApplicationContext private val ctx: Context,
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
            api.getQuestions(amount, category, difficulty, type)
        )
        return if (domainModel != null && domainModel.questions.isNotEmpty()) {
            domainModel
        } else {
            throw EmptyQuestionsListException(ctx.getString(R.string.empty_questions_list_response))
        }
    }

    override suspend fun getCategories(): CategoriesList {
        if (categoriesList != null) {
            return categoriesList!!
        }
        val categories = categoriesMapper.mapResponseToCategoriesList(
            api.getCategories()
        )
        if (categories != null && categories.categoriesList.isNotEmpty()) {
            categoriesList = categories
            return categoriesList!!
        } else {
            throw EmptyCategoriesListException(ctx.getString(R.string.empty_categories_list_response))
        }
    }

}