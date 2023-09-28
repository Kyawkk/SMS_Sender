package com.kyawzinlinn.smssender.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kyawzinlinn.smssender.model.MessageDTO

@Database(entities = [MessageDTO::class], version = 1, exportSchema = false)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun messageDao() : MessageDao

    companion object {
        @Volatile
        private var INSTANCE: MessageDatabase? = null

        fun getDatabase(context: Context): MessageDatabase{
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(context, MessageDatabase::class.java, "message_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}