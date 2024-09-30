package ru.kpfu.itis.gimaletdinova.quizapp.util

object NetworkConstants {
    const val HEADER_AUTHORIZATION = "Authorization"
    const val TOKEN_TYPE = "Bearer"
    const val STOMP_CONNECTION_PATH = "game-websocket/websocket"
    const val STOMP_SUBSCRIBE_PATH = "/topic/game/"
    const val STOMP_SEND_PATH = "/app/game/"
}