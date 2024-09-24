package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.ChangeThemeManager
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys.NIGHT_MODE_KEY
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys.THEME_CHANGED_KEY
import javax.inject.Inject

class ChangeThemeManagerImpl @Inject constructor(
    private val prefs: DataStore<Preferences>,
    @ApplicationContext private val context: Context
) : ChangeThemeManager {

    override suspend fun changeTheme() {
        val changeTo = isNightTheme().not()
        prefs.edit {
            it[THEME_CHANGED_KEY] = true
            it[NIGHT_MODE_KEY] = changeTo
        }
        setTheme(changeTo)
    }

    override suspend fun setCurrentTheme() {
        val themeChanged = prefs.data.firstOrNull()?.get(THEME_CHANGED_KEY) ?: false

        if (themeChanged.not()) {
            withContext(Dispatchers.Main) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            }
            prefs.edit {
                it[NIGHT_MODE_KEY] = isSystemNightTheme()
            }
        } else {
            setTheme(isNightTheme())
            prefs.edit {
                it[THEME_CHANGED_KEY] = false
            }

        }
    }

    override fun isNightThemeFlow(): Flow<Boolean> {
        return prefs.data.map { it[NIGHT_MODE_KEY] ?: false }
    }

    private suspend fun isNightTheme(): Boolean {
        return prefs.data.firstOrNull()?.get(NIGHT_MODE_KEY) ?: false
    }

    private fun isSystemNightTheme(): Boolean {
        return context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }


    private suspend fun setTheme(isNightTheme: Boolean) {
        val mode =
            if (isNightTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        withContext(Dispatchers.Main) {
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

}