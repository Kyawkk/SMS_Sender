package com.kyawzinlinn.smssender.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.smssender.data.local.repositories.SettingsPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsPreferencesViewModel(
    private val settingsPreferencesRepository: SettingsPreferencesRepository
) : ViewModel() {
    private var _isShowingDialogAgain = MutableStateFlow<Boolean>(false)
    val isShowingDialogAgain: StateFlow<Boolean> = _isShowingDialogAgain.asStateFlow()

    init {
        viewModelScope.launch {
            settingsPreferencesRepository.isWarningDialogShowingAgain.collectLatest {
                _isShowingDialogAgain.value = it
            }
        }
    }

    fun updateShowingDialogAgainStatus(showingDialogAgain: Boolean) {
        viewModelScope.launch {
            settingsPreferencesRepository.updateWarningDialogShowingStatus(showingDialogAgain)
        }
    }

}