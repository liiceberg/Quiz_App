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
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Score
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
    suspend fun getResults(@Query("code") code: String) : List<Score>

    @GET("api/room/game")
    suspend fun getGameContent(@Query("code") code: String) : TriviaResponse

    @GET("api/room/players")
    suspend fun getPlayers(@Query("code") code: String) : List<String>

    @GET("api/user/get/name")
    suspend fun getUsername(@Query("id") id: Long) : UsernameResponse

    @GET("api/user/update/name")
    suspend fun updateUsername(@Query("id") id: Long, @Query("name") name: String)
    @GET("api/user/get/rooms")
    suspend fun getUserRooms(@Query("id") id: Long) : List<Room>
}