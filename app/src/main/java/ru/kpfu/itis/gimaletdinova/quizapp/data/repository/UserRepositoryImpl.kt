package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.BadRequestException
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.JwtTokenManager
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.LoginRequest
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.AuthService
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val jwtTokenManager: JwtTokenManager
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
}