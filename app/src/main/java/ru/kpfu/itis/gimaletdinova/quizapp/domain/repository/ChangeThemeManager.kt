package ru.kpfu.itis.gimaletdinova.quizapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface ChangeThemeManager {
    suspend fun setCurrentTheme()
    suspend fun changeTheme()
    fun isNightThemeFlow() : Flow<Boolean>
}