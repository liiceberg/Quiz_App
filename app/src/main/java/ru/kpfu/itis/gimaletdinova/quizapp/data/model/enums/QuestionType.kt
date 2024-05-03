package ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums

import com.google.gson.annotations.SerializedName


enum class QuestionType {

    @SerializedName("multiple")
    MULTIPLE_CHOICE;

    override fun toString(): String {
        return when (this) {
            MULTIPLE_CHOICE -> "multiple"
        }
    }
}