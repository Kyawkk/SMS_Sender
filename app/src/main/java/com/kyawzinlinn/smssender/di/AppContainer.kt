package com.kyawzinlinn.smssender.di

import android.content.Context
import com.kyawzinlinn.smssender.data.MessageDao
import com.kyawzinlinn.smssender.data.MessageDatabase
import com.kyawzinlinn.smssender.data.MessageDatabaseRepository
import com.kyawzinlinn.smssender.data.MessageDatabaseRepositoryImpl
import com.kyawzinlinn.smssender.data.SmsRepository
import com.kyawzinlinn.smssender.data.WorkerSmsRepository

interface AppContainer {
    val repository: SmsRepository
    val messageDatabaseRepository: MessageDatabaseRepository
}

class AppDataContainer (context: Context): AppContainer{
    override val repository: SmsRepository = WorkerSmsRepository(context)
    override val messageDatabaseRepository: MessageDatabaseRepository by lazy {
        MessageDatabaseRepositoryImpl(MessageDatabase.getDatabase(context).messageDao())
    }
}
