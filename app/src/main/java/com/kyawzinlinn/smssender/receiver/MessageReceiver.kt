package com.kyawzinlinn.smssender.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.kyawzinlinn.smssender.data.MessageReceiverRepository
import com.kyawzinlinn.smssender.data.MessageReceiverRepositoryImpl
import com.kyawzinlinn.smssender.model.ReceivedMessage
import com.kyawzinlinn.smssender.ui.MessageReceiverViewModel

class MessageReceiver(private val onReceivedMessage: (ReceivedMessage) -> Unit = {}): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent?.action){

            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

            for (smsMessage in smsMessages) {
                val sender = smsMessage.originatingAddress
                val messageBody = smsMessage.messageBody
                onReceivedMessage(ReceivedMessage(sender.toString().replace("+959","09"),messageBody))
            }
        }
    }
}