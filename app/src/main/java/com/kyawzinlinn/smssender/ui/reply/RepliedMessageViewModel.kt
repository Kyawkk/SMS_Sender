package com.kyawzinlinn.smssender.ui.reply

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.smssender.data.RepliedMessagesRepository
import com.kyawzinlinn.smssender.model.RepliedMessageDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepliedMessageViewModel (
    private val repliedMessagesRepository: RepliedMessagesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(RepliedMessageUiState())
    val uiState: StateFlow<RepliedMessageUiState> = _uiState.asStateFlow()

    init {
        getRepliedMessages()
    }

    private fun getRepliedMessages(){
        _uiState.update {
            it.copy(
                repliedMessages = repliedMessagesRepository.getAllRepliedMessages()
            )
        }
    }

    fun getPhoneNumbersByPhoneNumber(phoneNumber: String) {
        _uiState.update {
            it.copy(
                repliedMessagesByPhoneNumber = repliedMessagesRepository.getRepliedMessagesByPhoneNumber(phoneNumber)
            )
        }
    }

    fun addRepliedMessages(phoneNumber: String, repliedMessages: List<RepliedMessageDto>){
        viewModelScope.launch (Dispatchers.IO) {
            repliedMessagesRepository.deleteRepliedMessagesByPhoneNumber(phoneNumber)
            repliedMessagesRepository.addRepliedMessage(repliedMessages)
        }
    }

    data class RepliedMessageUiState(
        val repliedMessages: Flow<List<RepliedMessageDto>> = emptyFlow(),
        val repliedMessagesByPhoneNumber: Flow<List<RepliedMessageDto>> = emptyFlow()
    )
}