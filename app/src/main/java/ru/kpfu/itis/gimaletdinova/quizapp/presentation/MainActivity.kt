package ru.kpfu.itis.gimaletdinova.quizapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.util.setCurrentTheme
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys.NIGHT_MODE_KEY
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            val isNightTheme = dataStore.data.firstOrNull()?.get(NIGHT_MODE_KEY) ?: false
            setCurrentTheme(isNightTheme)
        }
    }

}
