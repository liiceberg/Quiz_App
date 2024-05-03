package ru.kpfu.itis.gimaletdinova.quizapp.util

import androidx.datastore.preferences.preferencesKey
object PrefsKeys {
    val THEME_CHANGED_KEY = preferencesKey<Boolean>("THEME_CHANGED_KEY")
    val NIGHT_MODE_KEY = preferencesKey<Boolean>("NIGHT_MODE_KEY")
    val USERNAME_KEY = preferencesKey<String>("USERNAME_KEY")
    val TOTAL_QUESTIONS_KEY = preferencesKey<Int>("TOTAL_QUESTIONS_KEY")
    val USER_QUESTIONS_KEY = preferencesKey<Int>("USER_QUESTIONS_KEY")
}