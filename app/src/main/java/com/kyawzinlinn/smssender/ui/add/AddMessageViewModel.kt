package com.kyawzinlinn.smssender.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.smssender.data.MessageDatabaseRepository
import com.kyawzinlinn.smssender.model.MessageDto
import kotlinx.coroutines.launch

class AddMessageViewModel(
    private val messageDatabaseRepository: MessageDatabaseRepository
) : ViewModel() {
    fun addMessage(messageDTO: MessageDto){
        viewModelScope.launch {
            messageDatabaseRepository.addMessage(messageDTO)
        }
    }
}