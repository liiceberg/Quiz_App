package ru.kpfu.itis.gimaletdinova.quizapp.data.remote

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.remove
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys.ACCESS_JWT_KEY
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys.REFRESH_JWT_KEY
import javax.inject.Inject

class JwtTokenManagerImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    JwtTokenManager {

    override suspend fun saveAccessJwt(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_JWT_KEY] = token
        }
    }

    override suspend fun saveRefreshJwt(token: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_JWT_KEY] = token
        }
    }

    override suspend fun getAccessJwt(): String? {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_JWT_KEY]
        }.first()
    }

    override suspend fun getRefreshJwt(): String? {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_JWT_KEY]
        }.first()
    }

    override suspend fun clearAllTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_JWT_KEY)
            preferences.remove(REFRESH_JWT_KEY)
        }
    }
}