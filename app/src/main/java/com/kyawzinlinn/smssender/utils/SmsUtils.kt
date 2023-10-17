package com.kyawzinlinn.smssender.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object SmsUtils {
    fun requestSmsPermission(activity: Activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            // request permission
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.SEND_SMS),
                SMS_REQUEST_CODE
            )
        }
    }

    fun isPermissionGranted(activity: Activity) = ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED

    fun sendSms(phone: String, message: String){
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phone,null,message,null,null)
    }

    fun formatPhoneNumber(phoneNumber: String): String {
        return if (phoneNumber.startsWith("959") || phoneNumber.startsWith("+959")) phoneNumber.replace(Regex("[+]?959"),"09") else phoneNumber
    }
}