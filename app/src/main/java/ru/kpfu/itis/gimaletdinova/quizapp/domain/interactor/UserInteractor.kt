package ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.UserRepository
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {
    suspend fun login(email: String, password: String): Long {
        return withContext(dispatcher) {
            userRepository.login(email, password)
        }
    }

    suspend fun register(email: String, password: String) {
        return withContext(dispatcher) {
            userRepository.register(email, password)
        }
    }

    suspend fun getUsername(): String? {
        return withContext(dispatcher) {
            userRepository.getUsername()
        }
    }

    suspend fun getUserId(): Long {
        return withContext(dispatcher) {
            userRepository.getUserId()
        }
    }

    suspend fun setUsername(name: String) {
        return withContext(dispatcher) {
            userRepository.setUsername(name)
        }
    }

    suspend fun getRooms(): List<Room> {
        return withContext(dispatcher) {
            userRepository.getRooms()
        }
    }

    suspend fun logout() {
        return withContext(dispatcher) {
            userRepository.logout()
        }
    }
}