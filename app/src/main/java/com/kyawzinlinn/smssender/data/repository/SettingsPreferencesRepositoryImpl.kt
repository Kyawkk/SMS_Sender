package com.kyawzinlinn.smssender.data.repository

import com.kyawzinlinn.smssender.data.local.datastore.SettingsPreferences
import com.kyawzinlinn.smssender.data.local.repositories.SettingsPreferencesRepository

class SettingsPreferencesRepositoryImpl (
    private val settingsPreferences: SettingsPreferences
) : SettingsPreferencesRepository {
    override val isWarningDialogShowingAgain = settingsPreferences.isShowingDialogAgain()

    override suspend fun updateWarningDialogShowingStatus(showingDialogAgain: Boolean) = settingsPreferences.updateShowingDialogAgain(showingDialogAgain)

}