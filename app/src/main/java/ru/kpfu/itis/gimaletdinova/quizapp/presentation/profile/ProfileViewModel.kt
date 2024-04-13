package ru.kpfu.itis.gimaletdinova.quizapp.presentation.profile


import android.content.res.Resources.Theme
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys
import ru.kpfu.itis.gimaletdinova.quizapp.util.setCurrentTheme
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefs: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    val usernameFlow = prefs.data.map { it[PrefsKeys.USERNAME_KEY] ?: "user" }

    val totalQuestionsFlow = prefs.data.map { it[PrefsKeys.TOTAL_QUESTIONS_KEY] ?: 0 }

    val userQuestionsFlow = prefs.data.map { it[PrefsKeys.USER_QUESTIONS_KEY] ?: 0 }
    val themeFlow = prefs.data.map { it[PrefsKeys.NIGHT_MODE_KEY] ?: false }

    fun saveUsername(name: String) {
        viewModelScope.launch {
            withContext(dispatcher) {
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
}