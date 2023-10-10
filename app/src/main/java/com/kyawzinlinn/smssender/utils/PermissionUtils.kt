package com.kyawzinlinn.smssender.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionUtils {
    fun checkPermission(context: Context): Boolean {
        val isSmsPermissionGranted = checkPermissionStatus(context,android.Manifest.permission.SEND_SMS)
        val isReadSmsPermissionGranted = checkPermissionStatus(context,android.Manifest.permission.READ_SMS)
        val isNotificationPermissionGranted = checkPermissionStatus(context,android.Manifest.permission.POST_NOTIFICATIONS)
        return isNotificationPermissionGranted && isSmsPermissionGranted && isReadSmsPermissionGranted
    }

    private fun checkPermissionStatus(context: Context, permission: String): Boolean {
        val permissionStatus = ContextCompat.checkSelfPermission(context,permission)
        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }
}