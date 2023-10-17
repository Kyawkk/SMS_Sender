package com.kyawzinlinn.smssender.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kyawzinlinn.smssender.model.MessageDto
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("select * from messages")
    fun getAllMessages(): Flow<List<MessageDto>>

    @Query("select * from messages where phone_number LIKE :query")
    fun searchPhoneNumber(query: String): Flow<List<MessageDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessage(messageDTO: MessageDto)

    @Delete
    suspend fun deleteMessage(messageDTO: MessageDto)

    @Update
    suspend fun updateMessage(messageDTO: MessageDto)
}