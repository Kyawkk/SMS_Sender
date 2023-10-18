package com.kyawzinlinn.smssender.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object SmsUtils {

    fun sendSms(phone: String, message: String){
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phone,null,message,null,null)
    }

    fun formatPhoneNumber(phoneNumber: String): String {
        return if (phoneNumber.startsWith("959") || phoneNumber.startsWith("+959")) phoneNumber.replace(Regex("[+]?959"),"09") else phoneNumber
    }

    fun openAutoStartSettings(context: Context) {
        try {
            val intent = Intent()
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:${context.packageName}")
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle any exceptions that might occur when trying to open the settings.
        }
    }
}