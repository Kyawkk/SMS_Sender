package com.kyawzinlinn.smssender.data.local.repositories

import com.kyawzinlinn.smssender.data.model.RepliedMessageDto
import kotlinx.coroutines.flow.Flow

interface AutoRepliedMessagesRepository {
    fun getAllRepliedMessages(): Flow<List<RepliedMessageDto>>
    fun getRepliedMessagesByPhoneNumber(phoneNumber: String): Flow<List<RepliedMessageDto>>
    fun deleteRepliedMessagesByPhoneNumber(phoneNumber: String)
    suspend fun addRepliedMessage(repliedMessages: List<RepliedMessageDto>)
}