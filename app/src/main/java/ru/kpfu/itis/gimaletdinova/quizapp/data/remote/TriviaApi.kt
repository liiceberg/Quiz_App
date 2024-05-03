package ru.kpfu.itis.gimaletdinova.quizapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.response.CategoryResponse
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.response.TriviaResponse
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.QuestionType

interface TriviaApi {
    @GET("api.php")
    suspend fun getQuestions(
        @Query(value = "amount") amount: Int,
        @Query(value = "category") category: Int,
        @Query(value = "difficulty") difficulty: LevelDifficulty,
        @Query(value = "type") type: QuestionType,
    ): TriviaResponse?

    @GET("api_category.php")
    suspend fun getCategories() : CategoryResponse?

}