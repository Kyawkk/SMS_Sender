package com.kyawzinlinn.smssender.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.smssender.data.MessageDatabaseRepository
import com.kyawzinlinn.smssender.data.SmsRepository
import com.kyawzinlinn.smssender.model.Message
import com.kyawzinlinn.smssender.model.MessageDTO
import com.kyawzinlinn.smssender.model.toMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SmsViewModel(
    private val smsRepository: SmsRepository,
    private val messageRepository: MessageDatabaseRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow(SmsUiState())
    val uiState : StateFlow<SmsUiState> = _uiState

    init {
        getAllMessages()
    }

    fun sendSms(message: Message) {
        smsRepository.sendSms(message)
    }

    fun deleteMessage(message: MessageDTO){
        cancelSmsWorker(message)
        viewModelScope.launch {
            messageRepository.deleteMessage(message)
        }
    }

    fun updateMessage(messageToUpdate: MessageDTO, oldMessage: MessageDTO){
        cancelSmsWorker(oldMessage)
        smsRepository.sendSms(messageToUpdate.toMessage())
        viewModelScope.launch {
            messageRepository.updateMessage(messageToUpdate)
        }
    }

    fun cancelSmsWorker(message: MessageDTO){
        smsRepository.cancelSms(message.message + message.delayTime)
    }

    fun getAllMessages() {
        _uiState.update {
            it.copy(
                messages = messageRepository.getAllMessages()
            )
        }
    }

    data class SmsUiState(
        val messages: Flow<List<MessageDTO>> = emptyFlow()
    )
}