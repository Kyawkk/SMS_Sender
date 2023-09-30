@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.kyawzinlinn.smssender.ui.add

import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import android.widget.TimePicker
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
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
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.date_time.DateTimeDialog
import com.maxkeppeler.sheets.date_time.models.DateTimeSelection
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.FormatStyle
import java.util.Calendar
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
    var message by rememberSaveable { mutableStateOf("") }
    var selectedDateAndTime by rememberSaveable { mutableStateOf(LocalDateTime.now()) }
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    var showTimePickerDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(value = phoneNumber,
            onValueChange = { phoneNumber = it },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = "") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            placeholder = { Text(text = stringResource(id = R.string.phone_no_placeholder)) })
        TextField(value = message,
            onValueChange = { message = it },
            maxLines = 20,
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            placeholder = { Text(text = stringResource(id = R.string.message_no_placeholder)) })

        DateAndTimeInputLayout(onDateButtonClick = { showDatePickerDialog = true },
            onTimeButtonClick = { showTimePickerDialog = true })

        if (showDatePickerDialog) MaterialDatePickerDialog(onDismiss = {
            showDatePickerDialog = false
        }, onOkBtnClick = {
            showDatePickerDialog = false
        })
        
        if (showTimePickerDialog) TimePickerDialog(
            onCancel = { /*TODO*/ },
            onConfirm = { /*TODO*/ }) {
            TimePicker(state = rememberTimePickerState())
        }

        Button(
            onClick = {
                onAddMessageBtnClick(
                    MessageDTO(
                        0, phoneNumber, message, selectedDateAndTime.toFormattedDateTime(), false
                    )
                )
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.add_message))
        }
    }
}

@Composable
fun DateAndTimeInputLayout(
    onDateButtonClick: () -> Unit, onTimeButtonClick: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DateButton(
            icon = R.drawable.baseline_calendar_month_24,
            title = "Date",
            onButtonClick = onDateButtonClick,
            modifier = Modifier.weight(0.5f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        DateButton(
            icon = R.drawable.baseline_access_time_filled_24,
            title = "Time",
            onButtonClick = onTimeButtonClick,
            modifier = Modifier.weight(0.5f)
        )
    }
}

@Composable
fun DateButton(
    icon: Int, title: String, onButtonClick: () -> Unit, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(60.dp),
        shape = MaterialTheme.shapes.small,
        onClick = onButtonClick
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
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
            Log.d("TAG", "MaterialDatePickerDialog: ${formatter.format(Date(daterPickerState.selectedDateMillis!!))}")
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
fun TimePickerDialog() {
    val timePickerState = rememberTimePickerState()

    Dialog(onDismissRequest = { /*TODO*/ }, ) {
        TimePicker(state = timePickerState)
    }

    /*AlertDialog(onDismissRequest = { *//*TODO*//* }, confirmButton = {
        TextButton(onClick = {
            Log.d("TAG", "TimePickerDialog: ${timePickerState.hour}")
        }) {
            Text(text = "Ok")
        }
    })*/
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        val timePickerState = rememberTimePickerState()
        val selectedTime = remember {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
            calendar.set(Calendar.MINUTE, timePickerState.minute)
            calendar.time
        }
        val formattedTime = remember {
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(selectedTime)
        }

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
                            onConfirm(formattedTime)
                            Log.d("TAG", "TimePickerDialog: $formattedTime")
                        }
                    ) { Text("OK") }
                }
            }
        }
    }
}
