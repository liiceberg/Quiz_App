package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.LoginRequest
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.LoginResponse

interface AuthService {
    @POST("api/auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): Response<LoginResponse>

    @POST("api/user/register")
    suspend fun register(
        @Body body: LoginRequest
    ) : Response<Unit>

}