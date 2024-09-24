package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.interceptor

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.JwtManager
import ru.kpfu.itis.gimaletdinova.quizapp.util.NetworkConstants.HEADER_AUTHORIZATION
import ru.kpfu.itis.gimaletdinova.quizapp.util.NetworkConstants.TOKEN_TYPE
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(
    private val tokenManager: JwtManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getAccessJwt()
        }
        val request = chain.request().newBuilder()
        request.addHeader(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
        return chain.proceed(request.build())
    }
    
}