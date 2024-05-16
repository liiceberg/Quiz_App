package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response

import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty

data class RoomResponse(
    val code: String,
    val capacity: Int,
    val category: Int?,
    val difficulty: LevelDifficulty?
)