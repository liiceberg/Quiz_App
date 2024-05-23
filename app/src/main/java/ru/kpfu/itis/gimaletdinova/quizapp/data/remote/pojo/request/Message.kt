package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request

data class MessageDto (
    val sender: Long?,
    val code: Code,
    val message: String? = null,
    val wait: Int? = null,
    val score: Int? = null
)

enum class Code {
    JOIN, READY, EXIT, SCORE
}