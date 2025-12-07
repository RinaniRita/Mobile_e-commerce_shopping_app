package com.example.uwe_shopping_app.data.local.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("session_prefs")

class SessionManager(private val context: Context) {


    private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
    private val KEY_USER_EMAIL = stringPreferencesKey("user_email")

    val isLoggedIn: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[KEY_LOGGED_IN] ?: false }

    suspend fun setLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_LOGGED_IN] = loggedIn }
    }
    val userEmail: Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[KEY_USER_EMAIL] }

    suspend fun setUserEmail(email: String?) {
        context.dataStore.edit { prefs ->
            if (email != null) {
                prefs[KEY_USER_EMAIL] = email
            } else {
                prefs.remove(KEY_USER_EMAIL)
            }
        }
    }
}