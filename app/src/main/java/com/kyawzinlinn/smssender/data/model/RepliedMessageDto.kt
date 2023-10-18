package com.kyawzinlinn.smssender.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("replied_messages")
data class RepliedMessageDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("phone_number") val phoneNumber: String,
    @ColumnInfo("incoming_message") val incomingMessage: String,
    @ColumnInfo("replied_message") val repliedMessage: String,
    @ColumnInfo("is_all_numbers") val isAllNumbers : Boolean
)

fun List<RepliedMessageDto>.replaceAllPhoneNumbersAndEnabledAllNumbers(phoneNumber: String, allNumberEnabled: Boolean): List<RepliedMessageDto> {
    return map {
        it.copy(phoneNumber = phoneNumber, isAllNumbers = allNumberEnabled)
    }
}

fun List<RepliedMessageDto>.toRepliedMessage(): Map<String, List<RepliedMessageDto>> {
    return this.groupBy { it.phoneNumber }
}