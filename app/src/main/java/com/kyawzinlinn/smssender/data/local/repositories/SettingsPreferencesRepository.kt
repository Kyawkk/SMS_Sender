package com.kyawzinlinn.smssender.data.local.repositories

import kotlinx.coroutines.flow.Flow

interface SettingsPreferencesRepository {
    val isWarningDialogShowingAgain: Flow<Boolean>
    suspend fun updateWarningDialogShowingStatus(showingDialogAgain: Boolean)
}