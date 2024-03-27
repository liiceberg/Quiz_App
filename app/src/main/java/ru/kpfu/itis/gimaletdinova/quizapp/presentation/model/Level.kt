package ru.kpfu.itis.gimaletdinova.quizapp.presentation.model

import ru.kpfu.itis.gimaletdinova.quizapp.util.LevelDifficulty

data class Level(
    val number: Int,
    val difficulty: LevelDifficulty,
    val isBlocked: Boolean
) : Item()
