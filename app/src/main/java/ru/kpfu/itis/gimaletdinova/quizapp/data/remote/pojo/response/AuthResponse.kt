package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)