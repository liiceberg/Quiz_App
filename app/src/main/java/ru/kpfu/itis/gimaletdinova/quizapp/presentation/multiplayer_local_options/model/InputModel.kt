package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer_local_options.model

data class InputModel(
    var text: String = "",
    var isCorrect: Boolean = false,
    val position: Int
)
