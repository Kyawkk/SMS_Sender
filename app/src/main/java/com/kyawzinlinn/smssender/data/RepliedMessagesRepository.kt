package com.kyawzinlinn.smssender.data

import com.kyawzinlinn.smssender.model.RepliedMessageDto
import kotlinx.coroutines.flow.Flow

interface RepliedMessagesRepository {
    fun getAllRepliedMessages(): Flow<List<RepliedMessageDto>>
    fun getRepliedMessagesByPhoneNumber(phoneNumber: String): Flow<List<RepliedMessageDto>>
    fun deleteRepliedMessagesByPhoneNumber(phoneNumber: String)
    suspend fun addRepliedMessage(repliedMessages: List<RepliedMessageDto>)
}