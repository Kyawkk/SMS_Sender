package com.kyawzinlinn.smssender.domain.model

data class Message(
    val phone: String,
    val message: String,
    val delayTime: String,
    val isEveryDay: Boolean
)
