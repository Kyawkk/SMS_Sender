package com.kyawzinlinn.smssender.data

import com.kyawzinlinn.smssender.model.MessageDto
import kotlinx.coroutines.flow.Flow

interface MessageDatabaseRepository {
    fun getAllMessages(): Flow<List<MessageDto>>
    fun searchPhoneNumbers(query: String): Flow<List<MessageDto>>
    suspend fun addMessage(messageDTO: MessageDto)
    suspend fun updateMessage(messageDTO: MessageDto)
    suspend fun deleteMessage(messageDTO: MessageDto)
}