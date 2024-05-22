package ru.kpfu.itis.gimaletdinova.quizapp.util

import androidx.datastore.preferences.preferencesKey
object PrefsKeys {
    val THEME_CHANGED_KEY = preferencesKey<Boolean>("THEME_CHANGED_KEY")
    val NIGHT_MODE_KEY = preferencesKey<Boolean>("NIGHT_MODE_KEY")
    val USER_ID_KEY = preferencesKey<Long>("USER_ID_KEY")
    val TOTAL_QUESTIONS_KEY = preferencesKey<Int>("TOTAL_QUESTIONS_KEY")
    val USER_QUESTIONS_KEY = preferencesKey<Int>("USER_QUESTIONS_KEY")
    val ACCESS_JWT_KEY = preferencesKey<String>("ACCESS_JWT_KEY")
    val REFRESH_JWT_KEY = preferencesKey<String>("REFRESH_JWT_KEY")
}