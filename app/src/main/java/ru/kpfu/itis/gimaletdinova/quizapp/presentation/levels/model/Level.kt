package ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model

import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty

data class Level(
    val number: Int,
    val difficulty: LevelDifficulty,
    val isBlocked: Boolean
) : Item()
