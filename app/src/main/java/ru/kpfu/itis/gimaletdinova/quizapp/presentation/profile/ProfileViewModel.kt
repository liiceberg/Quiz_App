package ru.kpfu.itis.gimaletdinova.quizapp.presentation.profile


import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.remove
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.JwtTokenManager
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.ScoreInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.UserInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.UserScores
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys
import ru.kpfu.itis.gimaletdinova.quizapp.util.setCurrentTheme
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefs: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher,
    private val tokenManager: JwtTokenManager,
    private val userInteractor: UserInteractor,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val scoreInteractor: ScoreInteractor
) : ViewModel() {

    val usernameFlow = MutableStateFlow("")
    val scoresFlow = MutableStateFlow<UserScores?>(null)
    val themeFlow = prefs.data.map { it[PrefsKeys.NIGHT_MODE_KEY] ?: false }

    fun getUserInfo() {
        viewModelScope.launch {
            getUsername()
            getScores()
        }
    }

    private suspend fun getUsername() {
        runCatching(exceptionHandlerDelegate) {
            userInteractor.getUsername()
        }.onSuccess {
            if (it != null) {
                usernameFlow.value = it
            } else {
                usernameFlow.value = "user"
            }
        }
    }

    private suspend fun getScores() {
        runCatching(exceptionHandlerDelegate) {
            scoreInteractor.getScores()
        }.onSuccess {
            if (it != null) {
                scoresFlow.value = it
            } else {
                scoresFlow.value = UserScores(0, 0)
            }
        }
    }

    fun saveUsername(name: String) {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                userInteractor.setUsername(name)
            }.onSuccess {
                usernameFlow.value = name
            }
        }
    }

    fun changeTheme() {
        viewModelScope.launch {
            withContext(dispatcher) {
                prefs.edit {
                    it[PrefsKeys.THEME_CHANGED_KEY] = true
                    it[PrefsKeys.NIGHT_MODE_KEY] = !themeFlow.first()
                }
            }
            setCurrentTheme(themeFlow.first())
        }
    }

    suspend fun logout() {
        tokenManager.clearAllTokens()
        withContext(dispatcher) {
            prefs.edit {
                it.remove(PrefsKeys.USER_ID_KEY)
            }
        }
    }
}