package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.BadRequestException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.UserNotFoundException
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.JwtTokenManager
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.LoginRequest
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.AuthService
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.TriviaService
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.UserRepository
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val jwtTokenManager: JwtTokenManager,
    private val triviaService: TriviaService,
    private val dataStore: DataStore<Preferences>
): UserRepository {
    override suspend fun login(email: String, password: String) : Long {
        val response = authService.login(LoginRequest(email, password))
        if (response.isSuccessful) {
            response.body()?.let {
                jwtTokenManager.saveAccessJwt(it.accessToken)
                jwtTokenManager.saveRefreshJwt(it.refreshToken)
                return it.userId
            }
        }

        throw BadRequestException("The login has not been completed")
    }

    override suspend fun register(email: String, password: String) {
        authService.register(LoginRequest(email, password))
    }

    override suspend fun getUsername() : String {
        getUserId()?.let {
            return triviaService.getUsername(it).name
        }
        throw UserNotFoundException("User's id not found")
    }

    override suspend fun setUsername(name: String) {
        getUserId()?.let {
            triviaService.updateUsername(it, name)
        }
    }

    private suspend fun getUserId(): Long? {
        return dataStore.data.map {
            it[PrefsKeys.USER_ID_KEY]
        }.first()
    }
}