package com.example.digiboxtest.ui.theme
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val LOGGED_IN_USERNAME = stringPreferencesKey("logged_in_username") // Key baru
    }

    // Fungsi untuk menyimpan status dan username login
    suspend fun saveLoginSession(isLoggedIn: Boolean, username: String) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = isLoggedIn
            prefs[LOGGED_IN_USERNAME] = username
        }
    }

    // Fungsi untuk menghapus sesi login (logout)
    suspend fun clearLoginSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(IS_LOGGED_IN)
            prefs.remove(LOGGED_IN_USERNAME)
        }
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[IS_LOGGED_IN] == true }

    // Flow untuk membaca username
    val loggedInUsername: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[LOGGED_IN_USERNAME] }
}