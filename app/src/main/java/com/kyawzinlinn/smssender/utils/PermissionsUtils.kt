package com.kyawzinlinn.smssender.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionsUtils {
    private lateinit var context: Context
    fun isAllPermissionGranted(context: Context): Boolean {
        this.context = context
        return isPermissionGranted(Manifest.permission.SEND_SMS) && isPermissionGranted(Manifest.permission.READ_SMS) && isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED
    }
}