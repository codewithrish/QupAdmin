package app.qup.network.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import app.qup.util.common.JWT_ACCESS_TOKEN_PREF
import app.qup.util.common.JWT_REFRESH_TOKEN_PREF
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val JWT_ACCESS_TOKEN = stringPreferencesKey(JWT_ACCESS_TOKEN_PREF)
        private val JWT_REFRESH_TOKEN = stringPreferencesKey(JWT_REFRESH_TOKEN_PREF)
    }
    fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[JWT_ACCESS_TOKEN]
        }
    }
    fun getRefreshToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[JWT_REFRESH_TOKEN]
        }
    }
    suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[JWT_ACCESS_TOKEN] = token
        }
    }
    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[JWT_REFRESH_TOKEN] = token
        }
    }
    suspend fun deleteAccessToken() {
        dataStore.edit { preferences ->
            preferences.remove(JWT_ACCESS_TOKEN)
        }
    }
    suspend fun deleteRefreshToken() {
        dataStore.edit { preferences ->
            preferences.remove(JWT_REFRESH_TOKEN)
        }
    }
}