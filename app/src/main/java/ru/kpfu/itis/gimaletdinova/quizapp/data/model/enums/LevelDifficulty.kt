package ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums

import com.google.gson.annotations.SerializedName


enum class LevelDifficulty {
    @SerializedName("easy")
    EASY,

    @SerializedName("medium")
    MEDIUM,

    @SerializedName("hard")
    HARD;

    override fun toString(): String {
        return name.lowercase()
    }
}