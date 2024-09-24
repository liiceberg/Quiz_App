package ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.ChangeThemeManager
import javax.inject.Inject

class ChangeThemeInteractor @Inject constructor(
    private val changeThemeManager: ChangeThemeManager,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun setCurrentTheme() {
        withContext(dispatcher) {
            changeThemeManager.setCurrentTheme()
        }
    }

    suspend fun changeTheme() {
        withContext(dispatcher) {
            changeThemeManager.changeTheme()
        }
    }

    fun themeFlow() : Flow<Boolean> {
        return changeThemeManager.isNightThemeFlow()
    }

}