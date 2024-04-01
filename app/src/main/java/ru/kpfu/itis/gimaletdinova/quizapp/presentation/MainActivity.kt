package ru.kpfu.itis.gimaletdinova.quizapp.presentation

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.SHARED_PREFERENCES_NAME
import ru.kpfu.itis.gimaletdinova.quizapp.util.setTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val isNightTheme = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(getString(R.string.night_mode), false)
        setTheme(isNightTheme)
    }

}
