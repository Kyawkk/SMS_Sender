package com.kyawzinlinn.smssender.data

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.kyawzinlinn.smssender.model.Message
import com.kyawzinlinn.smssender.utils.KEY_MESSAGE
import com.kyawzinlinn.smssender.utils.KEY_PHONE_NUMBER
import com.kyawzinlinn.smssender.utils.SmsUtils
import com.kyawzinlinn.smssender.utils.toLocalDateTime
import com.kyawzinlinn.smssender.worker.SmsSenderWorker
import com.kyawzinlinn.smssender.worker.makeSmsSentNotification
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

class WorkerSmsRepository(private val context: Context) : SmsRepository {
    val workManager = WorkManager.getInstance(context)
    val isPermissionGranted = false
    override fun requestSmsPermission(activity: Activity) {
        SmsUtils.requestSmsPermission(activity)
    }

    override fun isPermissionGranted(activity: Activity): Boolean =
        SmsUtils.isPermissionGranted(activity)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun sendSms(message: Message) {
        val data = Data.Builder()
        data.putString(KEY_PHONE_NUMBER, message.phone)
        data.putString(KEY_MESSAGE, message.message)

        val now = LocalDateTime.now()
        val delayInSeconds =
            message.delayTime.toLocalDateTime().toEpochSecond(ZoneOffset.UTC) - now.toEpochSecond(
                ZoneOffset.UTC
            )

        if (message.isEveryDay) {
            val periodicWorkRequest = PeriodicWorkRequest.Builder(
                SmsSenderWorker::class.java, 1, TimeUnit.DAYS
            ).setInputData(data.build()).setInitialDelay(delayInSeconds, TimeUnit.SECONDS).build()

            workManager.enqueueUniquePeriodicWork(
                message.message + message.delayTime,
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicWorkRequest
            )
        } else {
            val oneTimeWorkRequest =
                OneTimeWorkRequestBuilder<SmsSenderWorker>().setInputData(data.build())
                    .setInitialDelay(delayInSeconds, TimeUnit.SECONDS).build()

            workManager.enqueueUniqueWork(
                message.message + message.delayTime, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest
            )
        }
    }

    override fun cancelSms(workerId: String) {
        workManager.cancelUniqueWork(workerId)
    }
}