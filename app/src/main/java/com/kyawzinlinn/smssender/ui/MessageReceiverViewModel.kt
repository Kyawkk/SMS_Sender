package com.kyawzinlinn.smssender.ui

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.smssender.data.MessageReceiverRepository
import com.kyawzinlinn.smssender.model.ReceivedMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MessageReceiverViewModel (private val messageReceiverRepository: MessageReceiverRepository): ViewModel() {
    val incomingMessages: StateFlow<ReceivedMessage> = MutableStateFlow(ReceivedMessage("",""))
}