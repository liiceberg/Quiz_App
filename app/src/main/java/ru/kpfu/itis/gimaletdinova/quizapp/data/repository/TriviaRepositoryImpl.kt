package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.EmptyQuestionsListException
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper.QuestionsListMapper
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.TriviaApi
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.isEmptyResponse
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.TriviaRepository
import javax.inject.Inject

class TriviaRepositoryImpl @Inject constructor(
    private val api: TriviaApi,
    @ApplicationContext private val ctx: Context,
    private val mapper: QuestionsListMapper
): TriviaRepository {
    override suspend fun getTrivia(
        amount: Int,
        category: Int,
        difficulty: String,
        type: String
    ): QuestionsList {
        val domainModel = mapper.mapResponseToQuestionsList(
            api.getQuestions(amount, category, difficulty, type)
        )
        return if (domainModel != null && domainModel.isEmptyResponse().not()) {
            domainModel
        } else {
            throw EmptyQuestionsListException(ctx.getString(R.string.empty_questions_list_response))
        }
    }
}