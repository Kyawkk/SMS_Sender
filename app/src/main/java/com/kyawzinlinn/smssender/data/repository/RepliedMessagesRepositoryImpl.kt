package com.kyawzinlinn.smssender.data.repository

import com.kyawzinlinn.smssender.data.local.dao.RepliedMessageDao
import com.kyawzinlinn.smssender.data.local.repositories.RepliedMessagesRepository
import com.kyawzinlinn.smssender.domain.model.RepliedMessageDto
import kotlinx.coroutines.flow.Flow

class RepliedMessagesRepositoryImpl (
    private val repliedMessageDao: RepliedMessageDao
): RepliedMessagesRepository {
    override suspend fun addRepliedMessage(repliedMessages: List<RepliedMessageDto>) = repliedMessageDao.addRepliedMessage(repliedMessages)

    override fun getRepliedMessagesByPhoneNumber(phoneNumber: String): Flow<List<RepliedMessageDto>> = repliedMessageDao.getRepliedMessages(phoneNumber)

    override fun deleteRepliedMessagesByPhoneNumber(phoneNumber: String) = repliedMessageDao.deleteRepliedMessagesByPhoneNumber(phoneNumber)

    override fun getAllRepliedMessages(): Flow<List<RepliedMessageDto>> = repliedMessageDao.getRepliedMessages()

}