package com.kyawzinlinn.smssender.model

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity("messages")
data class MessageDTO(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("phone_number") val phoneNumber: String,
    @ColumnInfo("message") val message: String,
    @ColumnInfo("delay_time") val delayTime: String,
    @ColumnInfo("is_every_day") val isEveryDay: Boolean
)

fun MessageDTO.toMessage(): Message{
    return Message(
        phoneNumber,
        message,
        delayTime,
        isEveryDay
    )
}
