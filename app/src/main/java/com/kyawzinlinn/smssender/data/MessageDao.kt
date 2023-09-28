package com.kyawzinlinn.smssender.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kyawzinlinn.smssender.model.MessageDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("select * from messages")
    fun getAllMessages(): Flow<List<MessageDTO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessage(messageDTO: MessageDTO)

    @Delete
    suspend fun deleteMessage(messageDTO: MessageDTO)

    @Update
    suspend fun updateMessage(messageDTO: MessageDTO)
}