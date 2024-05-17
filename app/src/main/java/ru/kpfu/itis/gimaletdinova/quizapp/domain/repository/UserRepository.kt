package ru.kpfu.itis.gimaletdinova.quizapp.domain.repository

interface UserRepository {
    suspend fun login(email: String, password: String) : Long
    suspend fun register(email: String, password: String)
    suspend fun getUsername(): String
    suspend fun setUsername(name: String)
}