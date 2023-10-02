package com.kyawzinlinn.smssender.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kyawzinlinn.smssender.model.Message
import com.kyawzinlinn.smssender.utils.KEY_IS_TIME_OVER
import com.kyawzinlinn.smssender.utils.KEY_MESSAGE
import com.kyawzinlinn.smssender.utils.KEY_PHONE_NUMBER
import com.kyawzinlinn.smssender.utils.SmsUtils

class SmsSenderWorker (private val context: Context, params: WorkerParameters) : CoroutineWorker (context,params) {
    override suspend fun doWork(): Result {
        val phoneNumber = inputData.getString(KEY_PHONE_NUMBER)
        val message = inputData.getString(KEY_MESSAGE)
        val isTimeOver = inputData.getBoolean(KEY_IS_TIME_OVER,false)

        return try {
            if (!isTimeOver) {
                makeSmsSentNotification("Successfully sent message to $phoneNumber \uD83E\uDD29 !" ?: "",context)
                SmsUtils.sendSms(phoneNumber!!,message!!)
            }
            Result.success()
        }catch (e: Exception){

            Result.failure()
        }
    }
}