package com.kyawzinlinn.smssender.model

fun List<RepliedMessageDto>.toRepliedMessage(): Map<String, List<RepliedMessageDto>> {
    val emptyPhoneNumbers = this.filter { it.isAllNumbers }
    /*val notAllNumberPhoneNumbers = this.filterNot { it.isAllNumbers }

    val groupedMessages = notAllNumberPhoneNumbers.groupBy { it.phoneNumber }

    val result = hashMapOf<String,List<RepliedMessageDto>>()

    groupedMessages.map { (phoneNumber, replies) ->
        val combinedReplies = replies + emptyPhoneNumbers
        result.put(phoneNumber,combinedReplies)
    }*/

    return this.groupBy { it.phoneNumber }
}
