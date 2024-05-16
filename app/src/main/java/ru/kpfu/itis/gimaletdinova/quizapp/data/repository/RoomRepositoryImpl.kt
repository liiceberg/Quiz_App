package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.EmptyQuestionsListException
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper.QuestionsListMapper
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.CreateRoomRequest
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.RoomResponse
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.TriviaService
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.RoomRepository
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val triviaService: TriviaService,
    private val questionsListMapper: QuestionsListMapper,
    @ApplicationContext private val ctx: Context
) : RoomRepository {
    override suspend fun createRoom(capacity: Int, categoryId: Int?, difficulty: LevelDifficulty?) : String {
        return triviaService.createRoom(CreateRoomRequest(capacity, categoryId, difficulty)).code
    }

    override suspend fun getAll(): List<RoomResponse> {
        return triviaService.getAll()
    }

    override suspend fun getResults(code: String) {
        triviaService.getResults(code)
    }

    override suspend fun getGameContent(code: String): QuestionsList {

        val domainModel = questionsListMapper.mapResponseToQuestionsList(
            triviaService.getGameContent(code)
        )
        return if (domainModel != null && domainModel.questions.isNotEmpty()) {
            domainModel
        } else {
            throw EmptyQuestionsListException(ctx.getString(R.string.empty_questions_list_response))
        }
    }
}