package com.kyawzinlinn.smssender.ui.reply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.smssender.data.local.repositories.AutoRepliedMessagesRepository
import com.kyawzinlinn.smssender.data.model.RepliedMessageDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AutoRepliedMessagesViewModel (
    private val autoRepliedMessagesRepository: AutoRepliedMessagesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(RepliedMessageUiState())
    val uiState: StateFlow<RepliedMessageUiState> = _uiState.asStateFlow()

    init {
        getRepliedMessages()
    }

    private fun getRepliedMessages(){
        _uiState.update {
            it.copy(
                repliedMessages = autoRepliedMessagesRepository.getAllRepliedMessages()
            )
        }
    }

    fun getPhoneNumbersByPhoneNumber(phoneNumber: String) {
        _uiState.update {
            it.copy(
                repliedMessagesByPhoneNumber = autoRepliedMessagesRepository.getRepliedMessagesByPhoneNumber(phoneNumber)
            )
        }
    }

    fun addRepliedMessages(phoneNumber: String, repliedMessages: List<RepliedMessageDto>){
        viewModelScope.launch (Dispatchers.IO) {
            autoRepliedMessagesRepository.deleteRepliedMessagesByPhoneNumber(phoneNumber)
            autoRepliedMessagesRepository.addRepliedMessage(repliedMessages)
        }
    }

    data class RepliedMessageUiState(
        val repliedMessages: Flow<List<RepliedMessageDto>> = emptyFlow(),
        val repliedMessagesByPhoneNumber: Flow<List<RepliedMessageDto>> = emptyFlow()
    )
}