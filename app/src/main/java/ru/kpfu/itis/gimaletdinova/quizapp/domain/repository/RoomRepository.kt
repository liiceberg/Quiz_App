package ru.kpfu.itis.gimaletdinova.quizapp.domain.repository

import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Score
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList

interface RoomRepository {
    suspend fun createRoom(capacity: Int, categoryId: Int?, difficulty: LevelDifficulty?) : String
    suspend fun getAll() : List<Room>
    suspend fun getResults(code: String) : List<Score>
    suspend fun getGameContent(code: String) : QuestionsList
}