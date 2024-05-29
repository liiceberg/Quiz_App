package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request

data class HttpError(
    val status: String,
    val timestamp: String,
    val message: String
)