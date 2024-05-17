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
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room
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

    @POST("api/room/create")
    suspend fun createRoom(@Body room: CreateRoomRequest) : CreateRoomResponse

    @GET("api/room/all")
    suspend fun getAll() : List<Room>

    @GET("api/room/results")
    suspend fun getResults(code: String)

    @GET("api/room/game")
    suspend fun getGameContent(code: String) : TriviaResponse

    @GET("api/user/get/name")
    suspend fun getUsername(id: Long) : UsernameResponse

    @GET("api/user/update/name")
    suspend fun updateUsername(@Query("id") id: Long, @Query("name") name: String)
}