package ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model

import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.LevelDifficulty

data class Level(
    val number: Int,
    val difficulty: LevelDifficulty,
    val isBlocked: Boolean
) : Item()
