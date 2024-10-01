package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request

import com.google.gson.Gson

data class Message (
    val sender: Long?,
    val code: Code,
    val message: String? = null,
    val wait: Int? = null,
    val score: Int? = null
) {

    enum class Code {
        JOIN, READY, EXIT, SCORE, ALIVE;
    }

    companion object {
        private val gson = Gson()

        fun toJson(message: Message): String = gson.toJson(message)

        fun fromJson(json: String) : Message = gson.fromJson(json, Message::class.java)
    }

}

