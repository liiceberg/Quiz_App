package ru.kpfu.itis.gimaletdinova.quizapp.util.enums


enum class LevelDifficulty {

    EASY, MEDIUM, HARD;
    override fun toString(): String {
        return name.lowercase()
    }
}