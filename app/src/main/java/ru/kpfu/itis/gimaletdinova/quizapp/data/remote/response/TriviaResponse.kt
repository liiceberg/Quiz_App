package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class TriviaResponse(
    @SerializedName("results")
    val questions: List<QuestionData>? = null
)

data class QuestionData(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("difficulty")
    val difficulty: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("question")
    val question: String? = null,
    @SerializedName("correct_answer")
    val correctAnswer: String? = null,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String>? = null,
)