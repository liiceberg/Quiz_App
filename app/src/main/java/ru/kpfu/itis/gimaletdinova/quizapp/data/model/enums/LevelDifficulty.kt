package ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.exception.LevelDifficultyNotFoundException
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants


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
            val minMediumLevel = GameConfigConstants.MEDIUM_LEVELS_NUMBER + GameConfigConstants.EASY_LEVELS_NUMBER
            val minHardLevel = minMediumLevel + GameConfigConstants.HARD_LEVELS_NUMBER
            return when (number) {
                in 1..GameConfigConstants.EASY_LEVELS_NUMBER -> EASY
                in GameConfigConstants.EASY_LEVELS_NUMBER..minMediumLevel -> MEDIUM
                in minMediumLevel..minHardLevel -> HARD
                else -> throw LevelDifficultyNotFoundException()
            }
        }
    }

}