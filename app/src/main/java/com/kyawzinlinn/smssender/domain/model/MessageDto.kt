package com.kyawzinlinn.smssender.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity("messages")
data class MessageDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("phone_number") val phoneNumber: String,
    @ColumnInfo("message") val message: String,
    @ColumnInfo("delay_time") val delayTime: String,
    @ColumnInfo("is_every_day") val isEveryDay: Boolean,
    @ColumnInfo("is_active") val isActive: Boolean
): Serializable

fun MessageDto.toMessage(): Message {
    return Message(
        phoneNumber,
        message,
        delayTime,
        isEveryDay
    )
}
