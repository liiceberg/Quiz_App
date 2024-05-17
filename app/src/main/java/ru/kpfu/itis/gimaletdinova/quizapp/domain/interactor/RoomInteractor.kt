package ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.RoomRepository
import javax.inject.Inject

class RoomInteractor @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val roomRepository: RoomRepository
) {
    suspend fun createRoom(capacity: Int, categoryId: Int?, difficulty: LevelDifficulty?) : String {
        return withContext(dispatcher) {
            roomRepository.createRoom(capacity, categoryId, difficulty)
        }
    }
    suspend fun getAll(): List<Room> {
        return withContext(dispatcher) {
            roomRepository.getAll()
        }
    }
    suspend fun getResults(code: String) {
        return withContext(dispatcher) {
            roomRepository.getResults(code)
        }
    }
    suspend fun getGameContent(code: String): QuestionsList {
        return withContext(dispatcher) {
            roomRepository.getGameContent(code)
        }
    }

}
