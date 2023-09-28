package com.kyawzinlinn.smssender.ui.screen

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kyawzinlinn.smssender.MainActivity
import com.kyawzinlinn.smssender.SmsSenderApplication
import com.kyawzinlinn.smssender.data.MessageDatabaseRepository
import com.kyawzinlinn.smssender.data.SmsRepository
import com.kyawzinlinn.smssender.model.Message
import com.kyawzinlinn.smssender.model.MessageDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SmsViewModel(
    private val repository: SmsRepository,
    private val messageRepository: MessageDatabaseRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow(SmsUiState())
    val uiState : StateFlow<SmsUiState> = _uiState

    init {
        getAllMessages()
    }

    fun sendSms(message: Message) {
        repository.sendSms(message)
    }

    fun getAllMessages() {
        _uiState.update {
            it.copy(
                messages = messageRepository.getAllMessages()
            )
        }
    }

    fun addMessage(messageDTO: MessageDTO) {
        viewModelScope.launch { messageRepository.addMessage(messageDTO) }
    }

    data class SmsUiState(
        val messages: Flow<List<MessageDTO>> = emptyFlow()
    )

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as SmsSenderApplication).container.repository
                val messageRepository = (this[APPLICATION_KEY] as SmsSenderApplication).container.messageDatabaseRepository

                SmsViewModel(repository,messageRepository)
            }
        }
    }
}