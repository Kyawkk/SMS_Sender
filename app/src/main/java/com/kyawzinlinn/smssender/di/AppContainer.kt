package com.kyawzinlinn.smssender.di

import android.content.Context
import com.kyawzinlinn.smssender.data.local.database.MessageDatabase
import com.kyawzinlinn.smssender.data.local.repositories.MessageDatabaseRepository
import com.kyawzinlinn.smssender.data.repository.MessageDatabaseRepositoryImpl
import com.kyawzinlinn.smssender.data.local.repositories.RepliedMessagesRepository
import com.kyawzinlinn.smssender.data.repository.RepliedMessagesRepositoryImpl
import com.kyawzinlinn.smssender.data.local.repositories.SmsRepository
import com.kyawzinlinn.smssender.data.repository.WorkerSmsRepository

interface AppContainer {
    val smsRepository: SmsRepository
    val messageDatabaseRepository: MessageDatabaseRepository
    val repliedMessagesRepository: RepliedMessagesRepository
}

class AppDataContainer (context: Context): AppContainer{
    override val smsRepository: SmsRepository = WorkerSmsRepository(context)
    override val messageDatabaseRepository: MessageDatabaseRepository by lazy {
        MessageDatabaseRepositoryImpl(MessageDatabase.getDatabase(context).messageDao())
    }
    override val repliedMessagesRepository: RepliedMessagesRepository by lazy {
        RepliedMessagesRepositoryImpl(MessageDatabase.getDatabase(context).repliedMessageDao())
    }
}
