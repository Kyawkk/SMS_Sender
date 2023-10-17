package com.kyawzinlinn.smssender.domain.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kyawzinlinn.smssender.MainActivity
import com.kyawzinlinn.smssender.R
import com.kyawzinlinn.smssender.utils.CHANNEL_ID
import com.kyawzinlinn.smssender.utils.NOTIFICATION_ID
import com.kyawzinlinn.smssender.utils.REQUEST_CODE

fun makeSmsSentNotification(
    message: String,
    context: Context
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            CHANNEL_ID,
            "SMS sent",
            importance
        )

        channel.description = message

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager?.createNotificationChannel(channel)
    }

    val pendingIntent = createPendingIntent(context)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("SMS")
        .setContentText(message)
        .setPriority(NotificationManager.IMPORTANCE_HIGH)
        .setVibrate(LongArray(1))
        .setContentIntent(pendingIntent)
        .setAutoCancel(false)

    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}

fun createPendingIntent(context: Context) : PendingIntent{
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    var flags = PendingIntent.FLAG_UPDATE_CURRENT

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        flags = flags or PendingIntent.FLAG_IMMUTABLE
    }

    return PendingIntent.getActivity(
        context,
        REQUEST_CODE,
        intent,
        flags
    )
}
