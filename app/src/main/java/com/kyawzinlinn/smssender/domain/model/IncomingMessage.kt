package com.kyawzinlinn.smssender.domain.model

data class IncomingMessage(
    val phoneNumber: String,
    val message: String
)
