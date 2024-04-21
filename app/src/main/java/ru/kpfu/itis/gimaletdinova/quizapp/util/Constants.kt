package ru.kpfu.itis.gimaletdinova.quizapp.util

object Constants {
    const val EASY_LEVELS_NUMBER = 10
    const val MEDIUM_LEVELS_NUMBER = 20
    const val HARD_LEVELS_NUMBER = 20
    const val LEVELS_NUMBER = EASY_LEVELS_NUMBER + MEDIUM_LEVELS_NUMBER + HARD_LEVELS_NUMBER

    const val MIN_PLAYERS_NUMBER = 2
    const val MAX_PLAYERS_NUMBER = 4

    const val QUESTIONS_NUMBER = 7
    const val QUESTION_TIME = 20_000L

    const val MIN_CORRECT_ANSWERS_NUMBER_TO_WIN = QUESTIONS_NUMBER - 2
    const val PLAYERS_QUESTIONS_NUMBER_PROPORTION = 3
}