package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.RefreshJwtRequest
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.AuthResponse

interface RefreshTokenService {
    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body token: RefreshJwtRequest): Response<AuthResponse>
}