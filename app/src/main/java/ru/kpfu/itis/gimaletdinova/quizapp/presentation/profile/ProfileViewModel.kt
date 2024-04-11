package ru.kpfu.itis.gimaletdinova.quizapp.presentation.profile


import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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

    val themeFlow = prefs.data.map { it[PrefsKeys.NIGHT_MODE_KEY] ?: false }

    val totalQuestionsFlow = prefs.data.map { it[PrefsKeys.TOTAL_QUESTIONS_KEY] ?: 0 }

    val userQuestionsFlow = prefs.data.map { it[PrefsKeys.USER_QUESTIONS_KEY] ?: 0 }
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
        val isNightTheme =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        viewModelScope.launch {
            withContext(dispatcher) {
                prefs.edit {
                    it[PrefsKeys.NIGHT_MODE_KEY] = !isNightTheme
                }
            }
            setCurrentTheme(!isNightTheme)
        }
    }
}