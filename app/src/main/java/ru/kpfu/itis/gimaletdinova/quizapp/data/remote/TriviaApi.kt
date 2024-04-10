package ru.kpfu.itis.gimaletdinova.quizapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.response.TriviaResponse

interface TriviaApi {
    @GET("api.php")
    suspend fun getQuestions(
        @Query(value = "amount") amount: Int,
        @Query(value = "category") category: Int,
        @Query(value = "difficulty") difficulty: String,
        @Query(value = "type") type: String,
    ): TriviaResponse?
}