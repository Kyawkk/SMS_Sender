package com.kyawzinlinn.smssender

import android.app.Application
import android.content.IntentFilter
import android.provider.Telephony
import android.util.Log
import com.kyawzinlinn.smssender.di.AppContainer
import com.kyawzinlinn.smssender.di.AppDataContainer
import com.kyawzinlinn.smssender.domain.model.IncomingMessage
import com.kyawzinlinn.smssender.domain.receiver.MessageReceiver
import com.kyawzinlinn.smssender.utils.SmsUtils
import kotlinx.coroutines.GlobalScope
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

    private suspend fun sendAutoReply(incomingMessage: IncomingMessage) {
        Log.d("TAG", "sendAutoReply: ${incomingMessage.phoneNumber}")
        container.autoRepliedMessagesRepository.getRepliedMessagesByPhoneNumber(incomingMessage.phoneNumber).collect { messages ->
            Log.d("TAG", "sendAutoReply: $messages")
            messages.forEach {
                Log.d("TAG", "sendAutoReply: ${incomingMessage.message.contains(it.incomingMessage)}")
                if (incomingMessage.message.contains(it.incomingMessage)) SmsUtils.sendSms(incomingMessage.phoneNumber,it.repliedMessage)
            }
        }
    }
}