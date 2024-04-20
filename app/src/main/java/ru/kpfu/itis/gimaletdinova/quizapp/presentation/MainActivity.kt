package ru.kpfu.itis.gimaletdinova.quizapp.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.game.QuestionViewModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.setCurrentTheme
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys.NIGHT_MODE_KEY
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys.THEME_CHANGED_KEY
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTheme()
    }

    private fun setTheme() {
        lifecycleScope.launch {
            val themeChanged = dataStore.data.firstOrNull()?.get(THEME_CHANGED_KEY) ?: false

            if (!themeChanged) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                dataStore.edit {
                    it[NIGHT_MODE_KEY] = isNightTheme()
                }
            } else {
                val isNightTheme = dataStore.data.firstOrNull()?.get(NIGHT_MODE_KEY) ?: false
                setCurrentTheme(isNightTheme)
                dataStore.edit {
                    it[THEME_CHANGED_KEY] = false
                }
            }
        }
    }

    private fun isNightTheme(): Boolean {
        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

}
