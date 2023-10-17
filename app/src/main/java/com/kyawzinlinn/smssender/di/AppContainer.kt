package com.kyawzinlinn.smssender.di

import android.content.Context
import com.kyawzinlinn.smssender.data.MessageDatabase
import com.kyawzinlinn.smssender.data.MessageDatabaseRepository
import com.kyawzinlinn.smssender.data.MessageDatabaseRepositoryImpl
import com.kyawzinlinn.smssender.data.MessageReceiverRepository
import com.kyawzinlinn.smssender.data.MessageReceiverRepositoryImpl
import com.kyawzinlinn.smssender.data.RepliedMessagesRepository
import com.kyawzinlinn.smssender.data.RepliedMessagesRepositoryImpl
import com.kyawzinlinn.smssender.data.SmsRepository
import com.kyawzinlinn.smssender.data.WorkerSmsRepository

interface AppContainer {
    val smsRepository: SmsRepository
    val messageDatabaseRepository: MessageDatabaseRepository
    val repliedMessagesRepository: RepliedMessagesRepository
    val messageReceiverRepository: MessageReceiverRepository
}

class AppDataContainer (context: Context): AppContainer{
    override val smsRepository: SmsRepository = WorkerSmsRepository(context)
    override val messageDatabaseRepository: MessageDatabaseRepository by lazy {
        MessageDatabaseRepositoryImpl(MessageDatabase.getDatabase(context).messageDao())
    }
    override val repliedMessagesRepository: RepliedMessagesRepository by lazy {
        RepliedMessagesRepositoryImpl(MessageDatabase.getDatabase(context).repliedMessageDao())
    }
    override val messageReceiverRepository: MessageReceiverRepository = MessageReceiverRepositoryImpl()
}
