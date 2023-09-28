@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)

package com.kyawzinlinn.smssender.ui.home

import android.Manifest
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.kyawzinlinn.smssender.AppViewModelProvider
import com.kyawzinlinn.smssender.model.MessageDTO
import com.kyawzinlinn.smssender.ui.navigation.NavigationDestination
import com.kyawzinlinn.smssender.ui.screen.SmsAppTopBar
import com.kyawzinlinn.smssender.ui.screen.SmsViewModel
import com.kyawzinlinn.smssender.utils.toFormattedDateTime

object HomeScreenDestination : NavigationDestination {
    override val route: String = "Home"
    override val title: String = "Home"
}

@Composable
fun HomeScreen(
    viewModel: SmsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToAddScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.SEND_SMS, onPermissionResult = {})

    val uiState by viewModel.uiState.collectAsState()
    val messages by uiState.messages.collectAsState(initial = emptyList())

    Scaffold(topBar = {
        SmsAppTopBar(
            title = HomeScreenDestination.title, showNavigateIcon = false
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = navigateToAddScreen) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
        }
    },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            if (permissionState.status == PermissionStatus.Granted) {
                MessageContentList(messages = messages, onMessageItemClicked = {

                })
            } else {
                Button(
                    onClick = {
                        permissionState.launchPermissionRequest()
                    }, modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Not Granted", textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

}

@Composable
private fun MessageContentList(
    messages: List<MessageDTO>,
    onMessageItemClicked: (MessageDTO) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(messages) {
            MessageListItem(messageDTO = it, onItemClicked = onMessageItemClicked, modifier = Modifier.animateItemPlacement())
        }
    }
}

@Composable
private fun MessageListItem(
    messageDTO: MessageDTO, onItemClicked: (MessageDTO) -> Unit, modifier: Modifier = Modifier
) {
    Card(modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        onClick = { onItemClicked(messageDTO) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = messageDTO.phoneNumber, style = MaterialTheme.typography.titleMedium)
            Text(text = messageDTO.message, style = MaterialTheme.typography.bodyMedium)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Text(text = messageDTO.delayTime)
            }
        }
    }
}