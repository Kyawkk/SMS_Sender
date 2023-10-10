package com.kyawzinlinn.smssender.data

import android.content.Intent
import com.kyawzinlinn.smssender.model.ReceivedMessage

interface MessageReceiverRepository {
    var receivedMessage : ReceivedMessage
}