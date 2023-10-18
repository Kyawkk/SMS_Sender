package com.kyawzinlinn.smssender.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.kyawzinlinn.smssender.data.local.repositories.ScheduledMessageWorkerRepository
import com.kyawzinlinn.smssender.domain.model.Message
import com.kyawzinlinn.smssender.utils.KEY_IS_TIME_OVER
import com.kyawzinlinn.smssender.utils.KEY_MESSAGE
import com.kyawzinlinn.smssender.utils.KEY_PHONE_NUMBER
import com.kyawzinlinn.smssender.utils.toLocalDateTime
import com.kyawzinlinn.smssender.domain.worker.SmsSenderWorker
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

class ScheduledMessageWorkerRepositoryImpl(context: Context) : ScheduledMessageWorkerRepository {
    val workManager = WorkManager.getInstance(context)

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

        data.putBoolean(KEY_IS_TIME_OVER,(delayInSeconds <= 0))

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