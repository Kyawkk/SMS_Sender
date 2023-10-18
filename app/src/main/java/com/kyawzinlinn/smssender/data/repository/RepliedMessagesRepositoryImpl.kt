package com.kyawzinlinn.smssender.data.repository

import com.kyawzinlinn.smssender.data.local.dao.RepliedMessageDao
import com.kyawzinlinn.smssender.data.local.repositories.AutoRepliedMessagesRepository
import com.kyawzinlinn.smssender.data.model.RepliedMessageDto
import kotlinx.coroutines.flow.Flow

class RepliedMessagesRepositoryImpl (
    private val repliedMessageDao: RepliedMessageDao
): AutoRepliedMessagesRepository {
    override suspend fun addRepliedMessage(repliedMessages: List<RepliedMessageDto>) = repliedMessageDao.addRepliedMessage(repliedMessages)

    override fun getRepliedMessagesByPhoneNumber(phoneNumber: String): Flow<List<RepliedMessageDto>> = repliedMessageDao.getRepliedMessages(phoneNumber)

    override fun deleteRepliedMessagesByPhoneNumber(phoneNumber: String) = repliedMessageDao.deleteRepliedMessagesByPhoneNumber(phoneNumber)

    override fun getAllRepliedMessages(): Flow<List<RepliedMessageDto>> = repliedMessageDao.getRepliedMessages()

}