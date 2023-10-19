package com.kyawzinlinn.smssender.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPreferences (private val context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    val showingDialogAgainKey = booleanPreferencesKey("is_showing_dialog_again")

    fun isShowingDialogAgain(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[showingDialogAgainKey] ?: true
        }
    }

    suspend fun updateShowingDialogAgain(isShowingDialogAgain: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[showingDialogAgainKey] = isShowingDialogAgain
        }
    }
}