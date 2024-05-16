package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.QuestionType
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.CreateRoomRequest
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.CategoryResponse
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.CreateRoomResponse
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.RoomResponse
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.TriviaResponse
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.UsernameResponse

interface TriviaService {

    @GET("api/trivia")
    suspend fun getQuestions(
        @Query(value = "amount") amount: Int,
        @Query(value = "category") category: Int,
        @Query(value = "difficulty") difficulty: LevelDifficulty,
        @Query(value = "type") type: QuestionType,
    ): TriviaResponse?

    @GET("api/trivia/categories")
    suspend fun getCategories() : CategoryResponse?

    @POST("room/create")
    suspend fun createRoom(@Body room: CreateRoomRequest) : CreateRoomResponse

    @GET("room/all")
    suspend fun getAll() : List<RoomResponse>

    @GET("room/results")
    suspend fun getResults(code: String)

    @GET("room/game")
    suspend fun getGameContent(code: String) : TriviaResponse

    @GET("user/get/name")
    suspend fun getUsername(id: Long) : UsernameResponse

    @GET("user/update/name")
    suspend fun updateUsername(id: Long, name: String) : UsernameResponse
}