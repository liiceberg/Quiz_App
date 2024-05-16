package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long
)