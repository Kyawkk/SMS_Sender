@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.kyawzinlinn.smssender.ui.add

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kyawzinlinn.smssender.AppViewModelProvider
import com.kyawzinlinn.smssender.R
import com.kyawzinlinn.smssender.model.MessageDTO
import com.kyawzinlinn.smssender.model.toMessage
import com.kyawzinlinn.smssender.ui.navigation.NavigationDestination
import com.kyawzinlinn.smssender.ui.screen.NavigateIconType
import com.kyawzinlinn.smssender.ui.screen.SmsAppTopBar
import com.kyawzinlinn.smssender.ui.screen.SmsViewModel
import com.kyawzinlinn.smssender.utils.toFormattedDateTime
import com.kyawzinlinn.smssender.utils.toFormattedTime
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

object AddMessageScreenDestination : NavigationDestination {
    override val route: String = "ADD"
    override val title: String = "Add Message"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddMessageScreen(
    viewModel: AddMessageViewModel = viewModel(factory = AppViewModelProvider.Factory),
    smsViewModel: SmsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit
) {
    Scaffold(modifier = modifier, topBar = {
        SmsAppTopBar(
            title = AddMessageScreenDestination.title,
            showNavigateIcon = true,
            navigateIconType = NavigateIconType.ADD_SCREEN,
            navigateUp = navigateUp
        )
    }) {
        MessageInputLayout(onAddMessageBtnClick = {
            smsViewModel.sendSms(it.toMessage())
            viewModel.addMessage(it)
            navigateUp()
        }, modifier = Modifier.padding(it))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageInputLayout(
    onAddMessageBtnClick: (MessageDTO) -> Unit, modifier: Modifier = Modifier
) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var isValidPhoneNumber by rememberSaveable { mutableStateOf(true) }

    var message by rememberSaveable { mutableStateOf("") }
    var isValidMessage by rememberSaveable { mutableStateOf(true) }

    var selectedDateAndTime by rememberSaveable { mutableStateOf(LocalDateTime.now()) }
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    var showTimePickerDialog by rememberSaveable { mutableStateOf(false) }

    var selectedDate by rememberSaveable { mutableStateOf("") }
    var isSelectedDateValid by rememberSaveable { mutableStateOf(true) }
    var selectedTime by rememberSaveable { mutableStateOf("") }
    var isSelectedTimeValid by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .animateContentSize()
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SmsInputField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.Phone,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            placeholderId = R.string.phone_no_placeholder,
            isValid = isValidPhoneNumber,
            errorMessage = "Please enter valid phone number!"
        )
        SmsInputField(
            value = message,
            onValueChange = { message = it },
            maxLines = 2,
            leadingIcon = Icons.Default.Email,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            placeholderId = R.string.message_no_placeholder,
            isValid = isValidMessage,
            errorMessage = "Message field must not be empty!"
        )

        DateAndTimeInputLayout(
            date = selectedDate,
            time = selectedTime,
            isDateValid = isSelectedDateValid,
            isTimeValid = isSelectedTimeValid,
            onDateButtonClick = { showDatePickerDialog = true },
            onTimeButtonClick = { showTimePickerDialog = true }
        )

        if (showDatePickerDialog) MaterialDatePickerDialog(
            onDismiss = {
                showDatePickerDialog = false
            }, onOkBtnClick = {
                selectedDate = it
                showDatePickerDialog = false
            }
        )

        if (showTimePickerDialog) TimePickerDialog(
            onCancel = { showTimePickerDialog = false },
            onConfirm = {
                selectedTime = it
                showTimePickerDialog = false
            }) {
        }

        Button(
            onClick = {

                isValidPhoneNumber = !phoneNumber.isNullOrEmpty()
                isValidMessage = !message.isNullOrEmpty()
                isSelectedDateValid = !selectedDate.isNullOrEmpty() || selectedDate.trim().length != 0
                isSelectedTimeValid = !selectedTime.isNullOrEmpty() || selectedTime.trim().length != 0

                if (isValidPhoneNumber && isValidMessage && isSelectedDateValid && isSelectedTimeValid) {
                    onAddMessageBtnClick(
                        MessageDTO(
                            0,
                            phoneNumber,
                            message,
                            ("$selectedDate $selectedTime"),
                            false
                        )
                    )
                }
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.add_message))
        }
    }
}

@Composable
fun SmsInputField(
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions,
    placeholderId: Int,
    isValid: Boolean,
    maxLines: Int = 1,
    errorMessage: String = "",
    modifier: Modifier
) {
    Column (
        modifier = modifier.animateContentSize()
    ) {
        TextField(
            value = value,
            maxLines = maxLines,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = "") },
            keyboardOptions = keyboardOptions,
            placeholder = { Text(text = stringResource(id = placeholderId)) }
        )

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
fun DateAndTimeInputLayout(
    date: String,
    time: String,
    isDateValid: Boolean,
    isTimeValid: Boolean,
    onDateButtonClick: () -> Unit,
    onTimeButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DateButton(
            icon = R.drawable.baseline_calendar_month_24,
            title = date,
            onButtonClick = onDateButtonClick,
            modifier = Modifier.weight(0.5f),
            isValid = isDateValid,
            errorMessage = "Choose date!"
        )
        Spacer(modifier = Modifier.width(12.dp))
        DateButton(
            icon = R.drawable.baseline_access_time_filled_24,
            title = time,
            onButtonClick = onTimeButtonClick,
            modifier = Modifier.weight(0.5f),
            isValid = isTimeValid,
            errorMessage = "Choose time!"
        )
    }
}

@Composable
fun DateButton(
    icon: Int,
    title: String,
    onButtonClick: () -> Unit,
    isValid: Boolean,
    errorMessage: String = "",
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier.animateContentSize (),
    ) {
        Card(
            modifier = Modifier.height(40.dp),
            shape = MaterialTheme.shapes.small,
            onClick = onButtonClick
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(12.dp),
                        painter = painterResource(icon),
                        contentDescription = ""
                    )
                    Text(
                        text = title
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = !isValid,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Text(errorMessage, color = Color.Red)
        }
    }
}

@Composable
fun MaterialDatePickerDialog(
    onDismiss: () -> Unit, onOkBtnClick: (String) -> Unit
) {
    val daterPickerState = rememberDatePickerState()
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
            val formattedDate = formatter.format(Date(daterPickerState.selectedDateMillis!!))
            onOkBtnClick(formattedDate.toString())
        }) {
            Text(text = "Ok")
        }
    }) {
        DatePicker(
            state = daterPickerState, showModeToggle = true, dateFormatter = DatePickerFormatter()
        )
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit,
    toggle: @Composable () -> Unit = {}
) {

    val timePickerState = rememberTimePickerState()

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {

        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            toggle()
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )

                TimePicker(state = timePickerState)

                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = {
                            onConfirm(timePickerState.toFormattedTime())
                        }
                    ) { Text("OK") }
                }
            }
        }
    }
}

data class Validation(
    var value: String,
    var isInvalid: Boolean
)
