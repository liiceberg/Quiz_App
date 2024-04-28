package ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.exception.LevelDifficultyNotFoundException
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants


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
    companion object {
        fun get(number: Int): LevelDifficulty {
            val minMediumLevel = Constants.MEDIUM_LEVELS_NUMBER + Constants.EASY_LEVELS_NUMBER
            val minHardLevel = minMediumLevel + Constants.HARD_LEVELS_NUMBER
            return when (number) {
                in 1..Constants.EASY_LEVELS_NUMBER -> EASY
                in Constants.EASY_LEVELS_NUMBER..minMediumLevel -> MEDIUM
                in minMediumLevel..minHardLevel -> HARD
                else -> throw LevelDifficultyNotFoundException()
            }
        }
    }

}