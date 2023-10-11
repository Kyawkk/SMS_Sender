package com.kyawzinlinn.smssender.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.smssender.data.MessageDatabaseRepository
import com.kyawzinlinn.smssender.data.SmsRepository
import com.kyawzinlinn.smssender.model.Message
import com.kyawzinlinn.smssender.model.MessageDTO
import com.kyawzinlinn.smssender.model.toMessage
import com.kyawzinlinn.smssender.ui.home.HomeScreenDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val smsRepository: SmsRepository,
    private val messageRepository: MessageDatabaseRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(SmsUiState())
    val uiState : StateFlow<SmsUiState> = _uiState.asStateFlow()

    init {
        getAllMessages()
    }

    fun sendMessage(message: Message) {
        smsRepository.sendSms(message)
    }

    fun updateTopBarUi(title: String, showNavigationIcon: Boolean, navigateUp: () -> Unit = {}){
        _uiState.update {
            it.copy(
                title = title,
                showNavigationIcon = showNavigationIcon,
                navigateUp =  navigateUp
            )
        }
    }

    fun updateFloatingActionButtonStatus(showFloatingActionButton: Boolean){
        _uiState.update {
            it.copy(
                showFloatingActionButton = showFloatingActionButton
            )
        }
    }

    fun deleteMessage(message: MessageDTO){
        cancelMessageSenderWorker(message)
        viewModelScope.launch {
            messageRepository.deleteMessage(message)
        }
    }

    fun updateMessage(messageToUpdate: MessageDTO, oldMessage: MessageDTO){
        cancelMessageSenderWorker(oldMessage)
        Log.d("TAG", "updateMessage: $messageToUpdate $oldMessage")
        smsRepository.sendSms(messageToUpdate.toMessage())
        viewModelScope.launch {
            messageRepository.updateMessage(messageToUpdate)
        }
    }

    fun toggleWorkStatus(isEnabled: Boolean, message: MessageDTO){
        viewModelScope.launch {
            messageRepository.updateMessage(message.copy(isActive = isEnabled))
        }
        if (isEnabled) sendMessage(message.toMessage())
        else cancelMessageSenderWorker(message)
    }

    private fun cancelMessageSenderWorker(message: MessageDTO){
        smsRepository.cancelSms(message.message + message.delayTime)
    }

    private fun getAllMessages() {
        _uiState.update {
            it.copy(
                messages = messageRepository.getAllMessages()
            )
        }
    }

    fun searchPhoneNumbers(query: String){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    phoneNumbers = messageRepository.searchPhoneNumbers(query)
                )
            }
        }
    }

    data class SmsUiState(
        val messages: Flow<List<MessageDTO>> = emptyFlow(),
        val phoneNumbers: Flow<List<MessageDTO>> = emptyFlow(),
        val query: String = "",
        val title: String = HomeScreenDestination.title,
        val showNavigationIcon: Boolean = false,
        val showFloatingActionButton: Boolean = true,
        val navigateUp: () -> Unit = {}
    )
}