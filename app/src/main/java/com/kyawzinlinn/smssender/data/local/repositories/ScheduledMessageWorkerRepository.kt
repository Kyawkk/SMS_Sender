package com.kyawzinlinn.smssender.data.local.repositories

import com.kyawzinlinn.smssender.domain.model.Message

interface ScheduledMessageWorkerRepository {
    fun sendSms(message: Message)
    fun cancelSms(workerId: String)
}