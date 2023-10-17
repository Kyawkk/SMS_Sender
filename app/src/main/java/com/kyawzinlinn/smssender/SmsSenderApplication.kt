package com.kyawzinlinn.smssender

import android.app.Application
import android.content.IntentFilter
import android.provider.Telephony
import android.util.Log
import com.kyawzinlinn.smssender.di.AppContainer
import com.kyawzinlinn.smssender.di.AppDataContainer
import com.kyawzinlinn.smssender.model.ReceivedMessage
import com.kyawzinlinn.smssender.receiver.MessageReceiver
import com.kyawzinlinn.smssender.utils.SmsUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SmsSenderApplication : Application () {
    lateinit var container: AppContainer
    lateinit var messageReceiver: MessageReceiver
    override fun onCreate() {
        super.onCreate()

        container = AppDataContainer(this)

        registerMessageReceiver()
    }

    private fun registerMessageReceiver() {
        val intent = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        messageReceiver = MessageReceiver{
            GlobalScope.launch {
                sendAutoReply(it)
            }
        }
        registerReceiver(messageReceiver,intent)
    }

    private suspend fun sendAutoReply(receivedMessage: ReceivedMessage) {
        Log.d("TAG", "sendAutoReply: ${receivedMessage.phoneNumber}")
        container.repliedMessagesRepository.getRepliedMessagesByPhoneNumber(receivedMessage.phoneNumber).collect { messages ->
            Log.d("TAG", "sendAutoReply: $messages")
            messages.forEach {
                Log.d("TAG", "sendAutoReply: ${receivedMessage.message.contains(it.incomingMessage)}")
                if (receivedMessage.message.contains(it.incomingMessage)) SmsUtils.sendSms(receivedMessage.phoneNumber,it.repliedMessage)
            }
        }
    }
}