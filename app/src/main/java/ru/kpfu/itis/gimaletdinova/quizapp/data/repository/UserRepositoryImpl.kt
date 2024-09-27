package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.remove
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.AppException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.AppException.SuchEmailAlreadyRegistered
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.HttpException.*
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.makeSafeApiCall
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.LoginRequest
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.AuthService
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.TriviaService
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.JwtManager
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.UserRepository
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val jwtManager: JwtManager,
    private val triviaService: TriviaService,
    private val dataStore: DataStore<Preferences>
) : UserRepository {

    override suspend fun login(email: String, password: String): Long {
        try {
            val loginResponse = makeSafeApiCall {
                authService.login(LoginRequest(email, password))
            }
            jwtManager.saveAccessJwt(loginResponse.accessToken)
            jwtManager.saveRefreshJwt(loginResponse.refreshToken)
            dataStore.edit {
                it[PrefsKeys.USER_ID_KEY] = loginResponse.userId
            }
            return loginResponse.userId
        } catch (ex: UnauthorizedException) {
            throw AppException.InvalidPassword("Invalid password")
        } catch (ex: NotFoundException) {
            throw AppException.EmailNotFound("Email not found")
        }
    }

    override suspend fun register(email: String, password: String) {
        try {
            makeSafeApiCall {
                authService.register(LoginRequest(email, password))
            }
        } catch (ex: ConflictException) {
            throw SuchEmailAlreadyRegistered("This email already registered")
        }
    }

    override suspend fun logout() {
        jwtManager.clearAllTokens()
        dataStore.edit {
            it.remove(PrefsKeys.USER_ID_KEY)
        }
    }

    override suspend fun getUsername(): String {
        return makeSafeApiCall {
            triviaService.getUsername(getUserId())
        }.name
    }

    override suspend fun setUsername(name: String) {
        makeSafeApiCall {
            triviaService.updateUsername(getUserId(), name)
        }
    }

    override suspend fun getRooms(): List<Room> {
        return makeSafeApiCall {
            triviaService.getUserRooms(getUserId())
        }
    }

    override suspend fun getUserId(): Long {
        return dataStore.data.map {
            it[PrefsKeys.USER_ID_KEY]
        }.first() ?: throw AppException.UserNotFoundException("User not found")
    }
}