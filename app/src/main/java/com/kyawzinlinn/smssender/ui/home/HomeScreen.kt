@file:OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class,
    ExperimentalPermissionsApi::class
)

package com.kyawzinlinn.smssender.ui.home

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.provider.Settings
import android.provider.Telephony
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.kyawzinlinn.smssender.R
import com.kyawzinlinn.smssender.model.MessageDto
import com.kyawzinlinn.smssender.receiver.MessageReceiver
import com.kyawzinlinn.smssender.ui.add.SmsNavigationType
import com.kyawzinlinn.smssender.ui.components.NoMessagesLayout
import com.kyawzinlinn.smssender.ui.navigation.NavigationDestination
import com.kyawzinlinn.smssender.ui.screen.HomeViewModel
import com.kyawzinlinn.smssender.utils.ScreenTitles
import kotlinx.coroutines.launch

object HomeScreenDestination : NavigationDestination {
    override val route: String = "Home"
    override val title: String = ScreenTitles.HOME.title
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navigateToAddScreen: (SmsNavigationType, MessageDto) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by homeViewModel.uiState.collectAsState()
    val messages by uiState.messages.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedScreen by remember { mutableStateOf(HomeScreenDestination.title) }
    var showEmptyScreen by remember { mutableStateOf(false) }

    val smsPermissionState =
        rememberPermissionState(permission = Manifest.permission.SEND_SMS, onPermissionResult = {})
    val readSmsPermissionState =
        rememberPermissionState(permission = Manifest.permission.READ_SMS, onPermissionResult = {})
    val notificationPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS,
        onPermissionResult = {}
    )

    val allPermissionGranted =
        smsPermissionState.status.isGranted && notificationPermissionState.status.isGranted && readSmsPermissionState.status.isGranted

    homeViewModel.updateTopBarUi(HomeScreenDestination.title, false)
    homeViewModel.updateBottomAppBarStatus(allPermissionGranted)

    LaunchedEffect(messages){
        showEmptyScreen = messages.isEmpty() && allPermissionGranted
    }

    Column(modifier = modifier) {
        when (selectedScreen) {
            ScreenTitles.HOME.title -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize()
            ) {

                if (showEmptyScreen){
                    NoMessagesLayout(
                        titleId = R.string.no_messages,
                        descriptionId = R.string.no_messages_desc
                    )
                }else {
                    MessageContentList(messages = messages, onMessageItemClicked = {

                    }, toggleSmsSenderWork = { isActive, message ->
                        homeViewModel.toggleWorkStatus(isActive, message)
                    }, onDeleteItemClick = {
                        homeViewModel.deleteMessage(it)
                    }, onUpdateItemClick = {
                        navigateToAddScreen(SmsNavigationType.UPDATE, it)
                    }
                    )
                }

                /*AnimatedVisibility(
                    visible = allPermissionGranted, enter = fadeIn(), exit = fadeOut()
                ) {

                }*/

                AnimatedVisibility(
                    !allPermissionGranted,
                    enter = scaleIn(animationSpec = tween(300), initialScale = 0.8f) + fadeIn(),
                    exit = scaleOut(animationSpec = tween(300), targetScale = 0.8f) + fadeOut()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        PermissionsRequestScreen(smsPermissionState = smsPermissionState,
                            notificationPermissionState = notificationPermissionState,
                            readSmsPermissionState = readSmsPermissionState,
                            onShouldShowRationale = {
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = it,
                                        actionLabel = "Open Settings",
                                        duration = SnackbarDuration.Indefinite
                                    )
                                    when (result) {
                                        SnackbarResult.ActionPerformed -> {
                                            val intent =
                                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                    data = Uri.fromParts(
                                                        "package", context.packageName, null
                                                    )
                                                }
                                            context.startActivity(intent)
                                        }

                                        SnackbarResult.Dismissed -> {

                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }

            ScreenTitles.REPLY.title -> {

            }
        }
    }
}

@Composable
private fun MessageContentList(
    messages: List<MessageDto>,
    onMessageItemClicked: (MessageDto) -> Unit,
    modifier: Modifier = Modifier,
    toggleSmsSenderWork: (Boolean, MessageDto) -> Unit,
    onDeleteItemClick: (MessageDto) -> Unit,
    onUpdateItemClick: (MessageDto) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(messages) { message ->
            MessageListItem(
                messageDTO = message,
                onItemClicked = onMessageItemClicked,
                modifier = Modifier.animateItemPlacement(),
                toggleSmsSenderWork = toggleSmsSenderWork,
                onDeleteItemClick = onDeleteItemClick,
                onUpdateItemClick = onUpdateItemClick
            )
        }
    }
}

@Composable
private fun MessageListItem(
    messageDTO: MessageDto,
    onItemClicked: (MessageDto) -> Unit,
    modifier: Modifier = Modifier,
    toggleSmsSenderWork: (Boolean, MessageDto) -> Unit,
    onDeleteItemClick: (MessageDto) -> Unit,
    onUpdateItemClick: (MessageDto) -> Unit
) {
    var isActive by remember { mutableStateOf(messageDTO.isActive) }
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        onClick = { onItemClicked(messageDTO) },
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = messageDTO.phoneNumber, style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = messageDTO.message, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (messageDTO.isEveryDay) messageDTO.delayTime.split(",")
                            .get(1) + " | Everyday"
                        else messageDTO.delayTime, style = MaterialTheme.typography.bodySmall
                    )
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    Switch(modifier = Modifier.padding(vertical = 12.dp),
                        checked = isActive,
                        onCheckedChange = {
                            isActive = !isActive
                            toggleSmsSenderWork(isActive, messageDTO)
                        })
                }
            }

            if (isExpanded) {
                ActionListItem(Icons.Default.Delete, "Delete", action = {
                    isExpanded = false
                    onDeleteItemClick(messageDTO)
                })
                ActionListItem(Icons.Default.Edit, "Edit", action = {
                    isExpanded = false
                    onUpdateItemClick(messageDTO)
                })
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ActionListItem(
    icon: ImageVector,
    title: String,
    action: () -> Unit,
    modifier: Modifier = Modifier.padding(horizontal = 12.dp)
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
            .clickable {
                action()
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(Modifier.width(12.dp))
        Text(title)
    }
}