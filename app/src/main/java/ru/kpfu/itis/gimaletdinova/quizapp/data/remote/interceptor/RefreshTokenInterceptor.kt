package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.interceptor
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.JwtManager
import javax.inject.Inject
class RefreshTokenInterceptor @Inject constructor(
    private val tokenManager: JwtManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getRefreshJwt()
        }
        val request = chain.request().newBuilder()
        request.addHeader(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
        return chain.proceed(request.build())
    }

    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }
}