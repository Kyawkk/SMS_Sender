package com.kyawzinlinn.smssender.model

import java.time.LocalDateTime

data class Message(
    val phone: String,
    val message: String,
    val delayTime: String,
    val isEveryDay: Boolean
)
