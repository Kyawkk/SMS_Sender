@file:OptIn(ExperimentalMaterial3Api::class)

package com.kyawzinlinn.smssender.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SpeakerNotesOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.wear.compose.material.Checkbox
import androidx.wear.compose.material.CheckboxDefaults
import com.kyawzinlinn.smssender.R
import com.kyawzinlinn.smssender.utils.SmsUtils

@Composable
fun SmsInputField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    placeholderId: Int,
    isValid: Boolean,
    label: String,
    onKeyboardActionDone: () -> Unit = {},
    maxLines: Int = 1,
    errorMessage: String = "",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.animateContentSize()
    ) {
        OutlinedTextField(
            value = value,
            maxLines = maxLines,
            label = { Text(label) },
            onValueChange = onValueChange,
            keyboardActions = KeyboardActions(
                onDone = { onKeyboardActionDone() }
            ),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            placeholder = { Text(text = stringResource(id = placeholderId)) }
        )

        Spacer(Modifier.height(4.dp))
        AnimatedVisibility(
            visible = !isValid,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun NoMessagesLayout(
    emptyIcon: ImageVector = Icons.Rounded.SpeakerNotesOff,
    titleId: Int,
    descriptionId: Int,
    modifier: Modifier = Modifier.padding(16.dp)
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = emptyIcon,
                modifier = Modifier.size(100.dp),
                contentDescription = null,
                tint = Color.Gray
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(titleId),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight(600)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(descriptionId),
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EnableAutoStartSettingDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    var dontShowAgain by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        title = { Text("Important") },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.enable_auto_start_setting)
                )
                Spacer(Modifier.height(16.dp))
                Row {
                    Checkbox(
                        checked = dontShowAgain,
                        colors = CheckboxDefaults.colors(uncheckedBoxColor = MaterialTheme.colorScheme.primary),
                        onCheckedChange = {
                            dontShowAgain = it
                        }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Don't show this message again")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    SmsUtils.openAutoStartSettings(context)
                }
            ) {
                Text("Open Setting")
            }
        },
        modifier = modifier
    )
}

@Composable
@Preview(showBackground = true)
fun EnableAutoStartSettingDialogPreview() {
    EnableAutoStartSettingDialog(
        onDismissRequest = {}
    )
}