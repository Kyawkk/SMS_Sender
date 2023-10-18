package com.kyawzinlinn.smssender.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.smssender.data.local.repositories.MessageDatabaseRepository
import com.kyawzinlinn.smssender.data.local.repositories.ScheduledMessageWorkerRepository
import com.kyawzinlinn.smssender.domain.model.Message
import com.kyawzinlinn.smssender.data.model.MessageDto
import com.kyawzinlinn.smssender.data.model.toMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScheduledMessagesViewModel(
    private val scheduledMessageWorkerRepository: ScheduledMessageWorkerRepository,
    private val messageDatabaseRepository: MessageDatabaseRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(ScheduledMessagesUiState())
    val uiState: StateFlow<ScheduledMessagesUiState> = _uiState.asStateFlow()

    init {
        getAllMessages()
    }

    fun sendMessage(message: Message) {
        scheduledMessageWorkerRepository.sendSms(message)
    }

    fun addMessage(messageDTO: MessageDto){
        viewModelScope.launch {
            messageDatabaseRepository.addMessage(messageDTO)
        }
    }

    fun deleteMessage(message: MessageDto){
        cancelMessageSenderWorker(message)
        viewModelScope.launch {
            messageDatabaseRepository.deleteMessage(message)
        }
    }

    fun updateMessage(messageToUpdate: MessageDto, oldMessage: MessageDto){
        cancelMessageSenderWorker(oldMessage)
        scheduledMessageWorkerRepository.sendSms(messageToUpdate.toMessage())
        viewModelScope.launch {
            messageDatabaseRepository.updateMessage(messageToUpdate)
        }
    }

    fun toggleWorkStatus(isEnabled: Boolean, message: MessageDto){
        viewModelScope.launch {
            messageDatabaseRepository.updateMessage(message.copy(isActive = isEnabled))
        }
        if (isEnabled) sendMessage(message.toMessage())
        else cancelMessageSenderWorker(message)
    }

    private fun cancelMessageSenderWorker(message: MessageDto){
        scheduledMessageWorkerRepository.cancelSms(message.message + message.delayTime)
    }

    private fun getAllMessages() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    messages = messageDatabaseRepository.getAllMessages()
                )
            }
        }
    }

    data class ScheduledMessagesUiState(
        val messages: Flow<List<MessageDto>> = emptyFlow()
    )
}