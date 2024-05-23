package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.QuestionType

data class TriviaResponse(
    @SerializedName("results")
    val questions: List<QuestionData>
)

data class QuestionData(
    @SerializedName("type")
    val type: QuestionType,
    @SerializedName("difficulty")
    val difficulty: LevelDifficulty,
    @SerializedName("category")
    val category: String,
    @SerializedName("question")
    val question: String,
    @SerializedName("correct_answer")
    val correctAnswer: String,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String>
)