package com.kyawzinlinn.smssender.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kyawzinlinn.smssender.data.local.repositories.SettingsPreferencesRepository
import com.kyawzinlinn.smssender.ui.home.HomeScreenDestination
import com.kyawzinlinn.smssender.utils.PermissionsUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedUiViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState : StateFlow<SharedUiState> = _uiState.asStateFlow()

    fun showRationale(message: String) {
        _uiState.update {
            it.copy(
                showRationaleMessage = message
            )
        }
    }

    fun updateTopBarUi(title: String,showNavigationIcon: Boolean,navigateUp: () -> Unit = {}) {
        _uiState.value = _uiState.value.copy(
            title = title,
            showNavigationIcon = showNavigationIcon,
            navigateUp = navigateUp
        )
    }

    fun updatePermissionStatus(allPermissionGranted: Boolean){
        _uiState.update {
            it.copy(
                allPermissionGranted = allPermissionGranted
            )
        }
    }

    fun updateBottomAppBarStatus(showBottomAppBar: Boolean){
        _uiState.update {
            it.copy (
                showBottomAppBar = showBottomAppBar
            )
        }
    }

    fun updateFloatingActionButtonStatus(showFloatingActionButton: Boolean) {
        _uiState.value = _uiState.value.copy(showFloatingActionButton = showFloatingActionButton)
    }

    data class SharedUiState(
        val title: String = HomeScreenDestination.title,
        val allPermissionGranted: Boolean = true,
        val showNavigationIcon: Boolean = false,
        val showFloatingActionButton: Boolean = true,
        val showBottomAppBar: Boolean = false,
        val showRationaleMessage: String = "",
        val navigateUp: () -> Unit = {}
    )
}