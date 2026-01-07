package com.example.uwe_shopping_app.data.local.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("session_prefs")

class SessionManager(private val context: Context) {

    private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
    private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    private val KEY_USER_ID = intPreferencesKey("user_id")
    private val KEY_USER_PHONE = stringPreferencesKey("user_phone")
    private val KEY_NOTIFICATIONS_ENABLED =
        booleanPreferencesKey("notifications_enabled")

    val isLoggedIn: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[KEY_LOGGED_IN] ?: false }

    val userEmail: Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[KEY_USER_EMAIL] }

    val userId: Flow<Int?> =
        context.dataStore.data.map { prefs -> prefs[KEY_USER_ID] }
    
    val userPhone: Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[KEY_USER_PHONE] }

    val notificationsEnabled: Flow<Boolean> =
        context.dataStore.data.map {
            it[KEY_NOTIFICATIONS_ENABLED] ?: true
        }

    suspend fun saveUserSession(id: Int, email: String, phone: String?) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = id
            prefs[KEY_USER_EMAIL] = email
            if (phone != null) prefs[KEY_USER_PHONE] = phone
            prefs[KEY_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = false
            prefs.remove(KEY_USER_EMAIL)
            prefs.remove(KEY_USER_ID)
            prefs.remove(KEY_USER_PHONE)
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit {
            it[KEY_NOTIFICATIONS_ENABLED] = enabled
        }
    }
}
