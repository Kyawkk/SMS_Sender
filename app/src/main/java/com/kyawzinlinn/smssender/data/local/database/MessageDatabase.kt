package com.kyawzinlinn.smssender.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kyawzinlinn.smssender.data.local.dao.ScheduledMessagesDao
import com.kyawzinlinn.smssender.data.local.dao.RepliedMessageDao
import com.kyawzinlinn.smssender.data.model.MessageDto
import com.kyawzinlinn.smssender.data.model.RepliedMessageDto

@Database(entities = [MessageDto::class, RepliedMessageDto::class], version = 2, exportSchema = false)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun scheduledMessagesDao() : ScheduledMessagesDao
    abstract fun repliedMessageDao(): RepliedMessageDao

    companion object {
        @Volatile
        private var INSTANCE: MessageDatabase? = null

        fun getDatabase(context: Context): MessageDatabase {
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(context, MessageDatabase::class.java, "message_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}