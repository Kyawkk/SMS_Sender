package com.kyawzinlinn.smssender.data

import com.kyawzinlinn.smssender.model.MessageDTO
import kotlinx.coroutines.flow.Flow

interface MessageDatabaseRepository {
    fun getAllMessages(): Flow<List<MessageDTO>>
    suspend fun addMessage(messageDTO: MessageDTO)
    suspend fun updateMessage(messageDTO: MessageDTO)
    suspend fun deleteMessage(messageDTO: MessageDTO)
}