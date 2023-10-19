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

    /**
     * Formats a phone number to match the Myanmar phone number format.
     * Myanmar phone numbers typically have the format "09-XXXX-XXXXX" or "959-XXX-XXXXXX"
     * @param phoneNumber The input number to be formatted
     * @return The formatted phone number
     * */
    fun formatPhoneNumber(phoneNumber: String): String {
        val pattern = "(^09|^959)".toRegex()
        val result = phoneNumber.replace(pattern){
            if (it.value.startsWith("09")) {
                "+959${it.value.substring(2)}"
            } else {
                "+959${it.value.substring(3)}"
            }
        }
        return result
    }


    fun openAutoStartSettings(context: Context) {
        val packageName = context.packageName
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}