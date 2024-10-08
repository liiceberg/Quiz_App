package ru.kpfu.itis.gimaletdinova.quizapp.data.remote

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.RefreshJwtRequest
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.RefreshTokenService
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.JwtManager
import ru.kpfu.itis.gimaletdinova.quizapp.util.NetworkConstants.HEADER_AUTHORIZATION
import ru.kpfu.itis.gimaletdinova.quizapp.util.NetworkConstants.TOKEN_TYPE
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
                try {
                    runBlocking {
                        val authResponse = makeSafeApiCall {
                            refreshTokenService.refreshToken(
                                RefreshJwtRequest(tokenManager.getRefreshJwt())
                            )
                        }

                        tokenManager.saveAccessJwt(authResponse.accessToken)
                        tokenManager.saveRefreshJwt(authResponse.refreshToken)
                        authResponse.accessToken
                    }
                } catch (ex: Exception) {
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

}