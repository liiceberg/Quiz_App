package ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories.model

import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants

data class Category(
    val name: String,
    val id: Int,
    val levelsNumber: Int,
    val totalLevelsNumber: Int = Constants.LEVELS_NUMBER
)