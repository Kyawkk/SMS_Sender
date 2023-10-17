package com.kyawzinlinn.smssender.data.local.repositories

import android.app.Activity
import com.kyawzinlinn.smssender.domain.model.Message

interface SmsRepository {
    fun sendSms(message: Message)
    fun cancelSms(workerId: String)
}