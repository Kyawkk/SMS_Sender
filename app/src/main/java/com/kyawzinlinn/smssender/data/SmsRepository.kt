package com.kyawzinlinn.smssender.data

import android.app.Activity
import com.kyawzinlinn.smssender.model.Message

interface SmsRepository {
    fun requestSmsPermission(activity: Activity)
    fun sendSms(message: Message)

    fun isPermissionGranted(activity: Activity): Boolean
}