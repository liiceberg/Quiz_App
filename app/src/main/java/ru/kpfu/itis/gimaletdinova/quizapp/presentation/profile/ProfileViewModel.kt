package ru.kpfu.itis.gimaletdinova.quizapp.presentation.profile


import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.remove
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.JwtTokenManager
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.UserRepository
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys
import ru.kpfu.itis.gimaletdinova.quizapp.util.setCurrentTheme
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefs: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher,
    private val tokenManager: JwtTokenManager,
    private val userRepository: UserRepository,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
) : ViewModel() {

    val usernameFlow = prefs.data.map { it[PrefsKeys.USERNAME_KEY] ?: getUsername() }

    val totalQuestionsFlow = prefs.data.map { it[PrefsKeys.TOTAL_QUESTIONS_KEY] ?: 0 }

    val userQuestionsFlow = prefs.data.map { it[PrefsKeys.USER_QUESTIONS_KEY] ?: 0 }
    val themeFlow = prefs.data.map { it[PrefsKeys.NIGHT_MODE_KEY] ?: false }

    private suspend fun getUsername() : String {
        runCatching(exceptionHandlerDelegate) {
            userRepository.getUsername()
        }.onSuccess {
            return it
        }
        return "user"
    }

    fun saveUsername(name: String) {
        viewModelScope.launch {
            withContext(dispatcher) {
                userRepository.setUsername(name)
                prefs.edit {
                    it[PrefsKeys.USERNAME_KEY] = name
                }
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

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearAllTokens()
            withContext(dispatcher) {
                prefs.edit {
                    it.remove(PrefsKeys.USERNAME_KEY)
                    it.remove(PrefsKeys.USER_ID_KEY)
                }
            }
        }
    }
}