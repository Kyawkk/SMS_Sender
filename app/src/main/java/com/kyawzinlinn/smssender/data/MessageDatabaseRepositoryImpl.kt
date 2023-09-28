package com.kyawzinlinn.smssender.data

import com.kyawzinlinn.smssender.model.MessageDTO
import kotlinx.coroutines.flow.Flow

class MessageDatabaseRepositoryImpl (private val messageDao: MessageDao) : MessageDatabaseRepository {
    override fun getAllMessages(): Flow<List<MessageDTO>> = messageDao.getAllMessages()

    override suspend fun addMessage(messageDTO: MessageDTO) {
        messageDao.addMessage(messageDTO)
    }

    override suspend fun updateMessage(messageDTO: MessageDTO) {
        messageDao.updateMessage(messageDTO)
    }

    override suspend fun deleteMessage(messageDTO: MessageDTO) {
        messageDao.deleteMessage(messageDTO)
    }
}