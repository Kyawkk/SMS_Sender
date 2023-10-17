package com.kyawzinlinn.smssender.data.local.repositories

import com.kyawzinlinn.smssender.domain.model.RepliedMessageDto
import kotlinx.coroutines.flow.Flow

interface RepliedMessagesRepository {
    fun getAllRepliedMessages(): Flow<List<RepliedMessageDto>>
    fun getRepliedMessagesByPhoneNumber(phoneNumber: String): Flow<List<RepliedMessageDto>>
    fun deleteRepliedMessagesByPhoneNumber(phoneNumber: String)
    suspend fun addRepliedMessage(repliedMessages: List<RepliedMessageDto>)
}