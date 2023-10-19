@file:OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class
)

package com.kyawzinlinn.smssender.ui.permission

import android.Manifest
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.PermissionStatus.Denied
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.kyawzinlinn.smssender.ui.SharedUiViewModel
import com.kyawzinlinn.smssender.ui.navigation.NavigationDestination
import com.kyawzinlinn.smssender.utils.ScreenTitles

object PermissionRequestScreenDestination : NavigationDestination {
    override val route: String = "Permission"
    override val title: String = ScreenTitles.REQUEST_PERMISSION.title

}

@Composable
fun PermissionsRequestScreen(
    sharedUiViewModel: SharedUiViewModel,
    modifier: Modifier = Modifier
) {
    var rationaleMessage by rememberSaveable { mutableStateOf("") }
    sharedUiViewModel.updateTopBarUi(
        title = PermissionRequestScreenDestination.title,
        showNavigationIcon = false,
        navigateUp = {}
    )
    sharedUiViewModel.updateBottomAppBarStatus(false)

    val smsPermissionState =
        rememberPermissionState(permission = Manifest.permission.SEND_SMS, onPermissionResult = {})
    val readSmsPermissionState =
        rememberPermissionState(permission = Manifest.permission.READ_SMS, onPermissionResult = {})
    val notificationPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS,
        onPermissionResult = {})

    sharedUiViewModel.updatePermissionStatus(smsPermissionState.status == PermissionStatus.Granted && readSmsPermissionState.status == PermissionStatus.Granted && notificationPermissionState.status == PermissionStatus.Granted)

    LaunchedEffect(rationaleMessage) {
        Log.d("TAG", "PermissionsRequestScreen: $rationaleMessage")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PermissionStatusListItem(
                icon = Icons.Default.Email,
                isGranted = smsPermissionState.status == PermissionStatus.Granted,
                title = "Enable SMS",
                onAllowButtonClick = {

                    if (smsPermissionState.status.shouldShowRationale || smsPermissionState.status == Denied(
                            true
                        )
                    ) {
                        rationaleMessage = "Open settings to allow sms permission."

                        sharedUiViewModel.showRationale(rationaleMessage)

                    } else {
                        smsPermissionState.launchPermissionRequest()
                    }
                },
                description = "Enable sms permission to send the messages in your list."
            )
            PermissionStatusListItem(
                icon = Icons.Default.Notifications,
                isGranted = notificationPermissionState.status == PermissionStatus.Granted,
                title = "Notification",
                onAllowButtonClick = {
                    Log.d(
                        "TAG",
                        "Permission requestss: ${notificationPermissionState.status} $rationaleMessage"
                    )
                    Log.d("TAG", "PermissionsRequestScreen: $rationaleMessage")
                    if (notificationPermissionState.status.shouldShowRationale || notificationPermissionState.status == Denied(
                            true
                        )
                    ) {
                        rationaleMessage = "Open settings to allow notification permission."
                        sharedUiViewModel.showRationale(rationaleMessage)
                    } else {
                        notificationPermissionState.launchPermissionRequest()
                    }
                },
                description = "Enable notification to notify when the message is sent."
            )
            PermissionStatusListItem(
                icon = Icons.Default.Message,
                isGranted = readSmsPermissionState.status == PermissionStatus.Granted,
                title = "Read SMS",
                onAllowButtonClick = {
                    Log.d(
                        "TAG",
                        "Permission requestss: ${readSmsPermissionState.status} $rationaleMessage"
                    )
                    Log.d("TAG", "PermissionsRequestScreen: $rationaleMessage")
                    if (readSmsPermissionState.status.shouldShowRationale || readSmsPermissionState.status == Denied(
                            true
                        )
                    ) {
                        rationaleMessage = "Open settings to allow receive sms permission."

                        sharedUiViewModel.showRationale(rationaleMessage)
                    } else {
                        readSmsPermissionState.launchPermissionRequest()
                    }
                },
                description = "Allow read sms permission in order to reply the incoming message."
            )
        }
    }
}

@Composable
private fun PermissionStatusListItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    isGranted: Boolean,
    title: String,
    onAllowButtonClick: () -> Unit,
    description: String
) {
    Card(
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon, contentDescription = null
            )
            Spacer(Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(Modifier.width(8.dp))
            Button(
                modifier = Modifier, enabled = !isGranted, onClick = onAllowButtonClick
            ) {
                Text(if (isGranted) "Granted" else "Allow")
            }
        }
    }
}