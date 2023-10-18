package com.kyawzinlinn.smssender.data.repository

import com.kyawzinlinn.smssender.data.local.dao.ScheduledMessagesDao
import com.kyawzinlinn.smssender.data.local.repositories.MessageDatabaseRepository
import com.kyawzinlinn.smssender.data.model.MessageDto
import kotlinx.coroutines.flow.Flow

class MessageDatabaseRepositoryImpl (private val messageDao: ScheduledMessagesDao) :
    MessageDatabaseRepository {
    override fun getAllMessages(): Flow<List<MessageDto>> = messageDao.getAllMessages()

    override fun searchPhoneNumbers(query: String): Flow<List<MessageDto>> = messageDao.getAllMessages()

    override suspend fun addMessage(messageDTO: MessageDto) {
        messageDao.addMessage(messageDTO)
    }

    override suspend fun updateMessage(messageDTO: MessageDto) {
        messageDao.updateMessage(messageDTO)
    }

    override suspend fun deleteMessage(messageDTO: MessageDto) {
        messageDao.deleteMessage(messageDTO)
    }
}