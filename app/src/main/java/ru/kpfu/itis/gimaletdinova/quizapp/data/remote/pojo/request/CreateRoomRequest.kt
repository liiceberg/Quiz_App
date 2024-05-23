package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request

import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty

data class CreateRoomRequest(
    val capacity: Int,
    val category: Int?,
    val difficulty: LevelDifficulty?
)