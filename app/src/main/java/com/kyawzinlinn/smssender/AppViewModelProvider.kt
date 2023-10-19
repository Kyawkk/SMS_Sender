package com.kyawzinlinn.smssender

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kyawzinlinn.smssender.ui.SettingsPreferencesViewModel
import com.kyawzinlinn.smssender.ui.reply.AutoRepliedMessagesViewModel
import com.kyawzinlinn.smssender.ui.home.ScheduledMessagesViewModel
import com.kyawzinlinn.smssender.ui.SharedUiViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SharedUiViewModel()
        }
        initializer {
            SettingsPreferencesViewModel(smsSenderApplication().container.settingsPreferencesRepository)
        }
        initializer {
            ScheduledMessagesViewModel(smsSenderApplication().container.scheduledMessageWorkerRepository, smsSenderApplication().container.messageDatabaseRepository)
        }

        initializer {
            AutoRepliedMessagesViewModel(smsSenderApplication().container.autoRepliedMessagesRepository)
        }
    }
}

fun CreationExtras.smsSenderApplication() : SmsSenderApplication = (this[APPLICATION_KEY] as SmsSenderApplication)