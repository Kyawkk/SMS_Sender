@file:OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class
)

package com.kyawzinlinn.smssender.ui.home

import android.Manifest
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.kyawzinlinn.smssender.AppViewModelProvider
import com.kyawzinlinn.smssender.data.DataSource
import com.kyawzinlinn.smssender.model.MessageDTO
import com.kyawzinlinn.smssender.model.toMessage
import com.kyawzinlinn.smssender.ui.navigation.NavigationDestination
import com.kyawzinlinn.smssender.ui.screen.SmsAppTopBar
import com.kyawzinlinn.smssender.ui.screen.SmsViewModel

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

                }, toggleSmsSenderWork = { isActive, message ->
                    if (isActive) viewModel.sendSms(message.toMessage())
                    else viewModel.cancelSmsWorker(message)
                },
                    onDeleteItemClick = {
                        viewModel.deleteMessage(it)
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
    modifier: Modifier = Modifier,
    toggleSmsSenderWork: (Boolean, MessageDTO) -> Unit,
    onDeleteItemClick: (MessageDTO) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(messages) { message ->
            MessageListItem(
                messageDTO = message,
                onItemClicked = onMessageItemClicked,
                modifier = Modifier.animateItemPlacement(),
                toggleSmsSenderWork = toggleSmsSenderWork,
                onDeleteItemClick = onDeleteItemClick
            )
        }
    }
}

@Composable
private fun MessageListItem(
    messageDTO: MessageDTO,
    onItemClicked: (MessageDTO) -> Unit,
    modifier: Modifier = Modifier,
    toggleSmsSenderWork: (Boolean, MessageDTO) -> Unit,
    onDeleteItemClick: (MessageDTO) -> Unit
) {
    var isActive by remember { mutableStateOf(true) }
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        onClick = { onItemClicked(messageDTO) },
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.animateContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = messageDTO.phoneNumber,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = messageDTO.message, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = messageDTO.delayTime, style = MaterialTheme.typography.bodySmall)
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    Switch(
                        modifier = Modifier.padding(vertical = 12.dp),
                        checked = isActive,
                        onCheckedChange = {
                            isActive = !isActive
                            toggleSmsSenderWork(isActive, messageDTO)
                        })
                }
            }

            if (isExpanded) {
                SelectDaysLayout()
                ActionListItem(
                    Icons.Default.Delete,
                    "Delete",
                    action = {
                        isExpanded = false
                        onDeleteItemClick(messageDTO)
                    }
                )
            }
        }
    }
}

@Composable
fun SelectDaysLayout(

    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(DataSource.days) {
            DayListItem(day = it, onDayClick = {})
        }
    }
}

@Composable
fun DayListItem(
    day: String,
    onDayClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEnabled by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(50),
        modifier = modifier,
        onClick = {
            isEnabled = !isEnabled
            onDayClick(isEnabled)
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) MaterialTheme.colorScheme.primary else Color.Transparent
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = day, Modifier.padding(12.dp))
        }
    }
}

@Composable
fun ActionListItem(
    icon: ImageVector,
    title: String,
    action: () -> Unit,
    modifier: Modifier = Modifier.padding(12.dp)
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
            .clickable {
                action()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(Modifier.width(12.dp))
        Text(title)
    }
}