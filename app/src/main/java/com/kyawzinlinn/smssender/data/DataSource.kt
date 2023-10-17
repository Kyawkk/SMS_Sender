package com.kyawzinlinn.smssender.data

import com.kyawzinlinn.smssender.model.RepliedMessageDto

object DataSource {
    val days = listOf(
        "M",
        "T",
        "W",
        "T",
        "F",
        "S",
        "S"
    )

    val defaultRepliedMessage = RepliedMessageDto(
        0,
        "",
        "",
        "",
        false
    )
}