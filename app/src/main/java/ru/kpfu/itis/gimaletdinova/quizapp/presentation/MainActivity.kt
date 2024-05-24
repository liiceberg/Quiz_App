package ru.kpfu.itis.gimaletdinova.quizapp.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.util.OnBackPressed
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys.NIGHT_MODE_KEY
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys.THEME_CHANGED_KEY
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys.USER_ID_KEY
import ru.kpfu.itis.gimaletdinova.quizapp.util.setCurrentTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTheme()

        setStartDestination()
    }

    private fun setStartDestination() {
        lifecycleScope.launch {
            val id = dataStore.data.map {
                it[USER_ID_KEY]
            }.firstOrNull()

            val navHost =
                supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment?
            val navController = navHost!!.navController

            val navInflater = navController.navInflater
            val graph = navInflater.inflate(R.navigation.navigation)

            if (id == null) {
                graph.setStartDestination(R.id.signInFragment)
            } else {
                graph.setStartDestination(R.id.startFragment)
            }

            navController.graph = graph
        }
    }

    override fun onBackPressed() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment?
        when(navHost?.navController?.currentDestination?.id) {
            R.id.roomFragment-> {
                (navHost.childFragmentManager.fragments[0] as? OnBackPressed)?.onBackPressed()
            }
            else -> {
                super.onBackPressed()
            }
        }
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
