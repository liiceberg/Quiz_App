package ru.kpfu.itis.gimaletdinova.quizapp.presentation.viewmodel


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.util.setTheme
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val ctx: Context,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _usernameFlow =
        MutableStateFlow(prefs.getString(ctx.getString(R.string.username), "user"))
    val usernameFlow get() = _usernameFlow.asStateFlow()

    private val _themeFlow =
        MutableStateFlow(prefs.getBoolean(ctx.getString(R.string.night_mode), false))
    val themeFlow get() = _themeFlow.asStateFlow()

    fun saveUsername(name: String) {
        if (prefs.edit()
                .putString(ctx.getString(R.string.username), name)
                .commit()
        ) {
            _usernameFlow.value = name
        }
    }

    fun changeTheme() {
        val isNightTheme =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        if (prefs.edit()
                .putBoolean(ctx.getString(R.string.night_mode), !isNightTheme)
                .commit()
        ) {
            _themeFlow.value = !isNightTheme
            setTheme(!isNightTheme)
        }

    }

    fun getUserQuestionsNumber(): Int = prefs.getInt(ctx.getString(R.string.user_questions), 0)
    fun getTotalQuestionsNumber(): Int = prefs.getInt(ctx.getString(R.string.total_questions), 0)
}