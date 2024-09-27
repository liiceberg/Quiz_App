package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.AppException
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.makeSafeApiCall
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper.QuestionsListMapper
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.CreateRoomRequest
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Score
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.TriviaService
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.RoomRepository
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val triviaService: TriviaService,
    private val questionsListMapper: QuestionsListMapper,
) : RoomRepository {

    override suspend fun createRoom(
        capacity: Int,
        categoryId: Int?,
        difficulty: LevelDifficulty?
    ): String {
        return makeSafeApiCall {
            triviaService.createRoom(
                CreateRoomRequest(capacity, categoryId, difficulty)
            )
        }.code
    }

    override suspend fun getAll(): List<Room> {
        return makeSafeApiCall { triviaService.getAll() }
    }

    override suspend fun getResults(code: String): List<Score> {
        return makeSafeApiCall { triviaService.getResults(code) }
    }

    override suspend fun getPlayers(code: String): List<String> {
        return makeSafeApiCall { triviaService.getPlayers(code) }
    }

    override suspend fun getGameContent(code: String): QuestionsList {

        val domainModel = questionsListMapper.mapResponseToQuestionsList(
            makeSafeApiCall { triviaService.getGameContent(code) }
        )
        return if (domainModel != null && domainModel.questions.isNotEmpty()) {
            domainModel
        } else {
            throw AppException.EmptyQuestionsListException("Questions not found")
        }
    }
}