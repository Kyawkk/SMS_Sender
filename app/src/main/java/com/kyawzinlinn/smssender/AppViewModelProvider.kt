package com.kyawzinlinn.smssender

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kyawzinlinn.smssender.ui.MessageReceiverViewModel
import com.kyawzinlinn.smssender.ui.add.AddMessageViewModel
import com.kyawzinlinn.smssender.ui.reply.RepliedMessageViewModel
import com.kyawzinlinn.smssender.ui.screen.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(smsSenderApplication().container.smsRepository, smsSenderApplication().container.messageDatabaseRepository)
        }

        initializer {
            AddMessageViewModel(smsSenderApplication().container.messageDatabaseRepository)
        }

        initializer {
            MessageReceiverViewModel(smsSenderApplication().container.messageReceiverRepository)
        }

        initializer {
            RepliedMessageViewModel(smsSenderApplication().container.repliedMessagesRepository)
        }
    }
}

fun CreationExtras.smsSenderApplication() : SmsSenderApplication = (this[APPLICATION_KEY] as SmsSenderApplication)