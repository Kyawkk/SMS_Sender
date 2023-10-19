package com.kyawzinlinn.smssender.di

import android.content.Context
import com.kyawzinlinn.smssender.data.local.database.MessageDatabase
import com.kyawzinlinn.smssender.data.local.datastore.SettingsPreferences
import com.kyawzinlinn.smssender.data.local.repositories.MessageDatabaseRepository
import com.kyawzinlinn.smssender.data.repository.MessageDatabaseRepositoryImpl
import com.kyawzinlinn.smssender.data.local.repositories.AutoRepliedMessagesRepository
import com.kyawzinlinn.smssender.data.repository.RepliedMessagesRepositoryImpl
import com.kyawzinlinn.smssender.data.local.repositories.ScheduledMessageWorkerRepository
import com.kyawzinlinn.smssender.data.local.repositories.SettingsPreferencesRepository
import com.kyawzinlinn.smssender.data.repository.ScheduledMessageWorkerRepositoryImpl
import com.kyawzinlinn.smssender.data.repository.SettingsPreferencesRepositoryImpl

interface AppContainer {
    val scheduledMessageWorkerRepository: ScheduledMessageWorkerRepository
    val messageDatabaseRepository: MessageDatabaseRepository
    val autoRepliedMessagesRepository: AutoRepliedMessagesRepository
    val settingsPreferencesRepository: SettingsPreferencesRepository
}

class AppDataContainer (context: Context): AppContainer{
    override val scheduledMessageWorkerRepository: ScheduledMessageWorkerRepository = ScheduledMessageWorkerRepositoryImpl(context)
    override val settingsPreferencesRepository: SettingsPreferencesRepository = SettingsPreferencesRepositoryImpl(
        SettingsPreferences(context)
    )
    override val messageDatabaseRepository: MessageDatabaseRepository by lazy {
        MessageDatabaseRepositoryImpl(MessageDatabase.getDatabase(context).scheduledMessagesDao())
    }
    override val autoRepliedMessagesRepository: AutoRepliedMessagesRepository by lazy {
        RepliedMessagesRepositoryImpl(MessageDatabase.getDatabase(context).repliedMessageDao())
    }
}
