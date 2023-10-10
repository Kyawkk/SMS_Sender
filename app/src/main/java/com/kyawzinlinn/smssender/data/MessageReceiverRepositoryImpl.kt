package com.kyawzinlinn.smssender.data

import android.content.Intent
import android.provider.Telephony
import com.kyawzinlinn.smssender.model.ReceivedMessage

class MessageReceiverRepositoryImpl: MessageReceiverRepository {
    override var receivedMessage: ReceivedMessage = ReceivedMessage("","")
}