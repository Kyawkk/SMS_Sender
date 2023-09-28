package com.kyawzinlinn.smssender

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kyawzinlinn.smssender.ui.add.AddMessageViewModel
import com.kyawzinlinn.smssender.ui.screen.SmsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SmsViewModel(smsSenderApplication().container.repository, smsSenderApplication().container.messageDatabaseRepository)
        }

        initializer {
            AddMessageViewModel(smsSenderApplication().container.messageDatabaseRepository)
        }
    }
}

fun CreationExtras.smsSenderApplication() : SmsSenderApplication = (this[APPLICATION_KEY] as SmsSenderApplication)