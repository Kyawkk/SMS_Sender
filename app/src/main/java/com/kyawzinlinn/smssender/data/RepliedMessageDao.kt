package com.kyawzinlinn.smssender.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kyawzinlinn.smssender.model.RepliedMessageDto
import kotlinx.coroutines.flow.Flow

@Dao
interface RepliedMessageDao {
    @Query("select * from replied_messages")
    fun getRepliedMessages(): Flow<List<RepliedMessageDto>>

    @Query("select * from replied_messages where phone_number like :phoneNumber")
    fun getRepliedMessagesByPhoneNumber(phoneNumber: String): Flow<List<RepliedMessageDto>>

    @Query("select * from replied_messages where is_all_numbers like true")
    fun getRepliedMessagesByAllNumber(): Flow<List<RepliedMessageDto>>

    fun getRepliedMessages(phoneNumber: String): Flow<List<RepliedMessageDto>> =
        if (phoneNumber.trim().isEmpty()) getRepliedMessagesByAllNumber()
        else getRepliedMessagesByPhoneNumber(phoneNumber)

    @Query("delete from replied_messages where phone_number like :phoneNumber")
    fun deleteRepliedMessagesByPhoneNumber(phoneNumber: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRepliedMessage(repliedMessages: List<RepliedMessageDto>)
}