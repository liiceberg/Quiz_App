package ru.kpfu.itis.gimaletdinova.quizapp.data.remote

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.RefreshJwtRequest
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.RefreshTokenService
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: JwtManager,
    private val refreshTokenService: RefreshTokenService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val currentToken = runBlocking {
            tokenManager.getAccessJwt()
        }
        synchronized(this) {
            val updatedToken = runBlocking {
                tokenManager.getAccessJwt()
            }
            val token = if (currentToken != updatedToken) updatedToken else {
                val newSessionResponse = runBlocking {
                    refreshTokenService.refreshToken(
                        RefreshJwtRequest(tokenManager.getRefreshJwt())
                    )
                }
                if (newSessionResponse.isSuccessful && newSessionResponse.body() != null) {
                    newSessionResponse.body()?.let { body ->
                        runBlocking {
                            tokenManager.saveAccessJwt(body.accessToken)
                            tokenManager.saveRefreshJwt(body.refreshToken)
                        }
                        body.accessToken
                    }
                } else {
                    null
                }
            }
            return if (token != null) {
                response.request
                    .newBuilder()
                    .header(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
                    .build()
            } else {
                null
            }
        }
    }
    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }
}