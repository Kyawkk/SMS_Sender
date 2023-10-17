@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.kyawzinlinn.smssender.ui.add

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Checkbox
import com.kyawzinlinn.smssender.AppViewModelProvider
import com.kyawzinlinn.smssender.R
import com.kyawzinlinn.smssender.domain.model.MessageDto
import com.kyawzinlinn.smssender.domain.model.toMessage
import com.kyawzinlinn.smssender.ui.components.SmsInputField
import com.kyawzinlinn.smssender.ui.navigation.NavigationDestination
import com.kyawzinlinn.smssender.ui.screen.HomeViewModel
import com.kyawzinlinn.smssender.utils.DateValidationUtils
import com.kyawzinlinn.smssender.utils.ScreenTitles
import com.kyawzinlinn.smssender.utils.toFormattedTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AddMessageScreenDestination : NavigationDestination {
    override val route: String = "ADD"
    override val title: String = ScreenTitles.ADD.title
}

enum class SmsNavigationType {
    ADD, UPDATE
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddMessageScreen(
    addMessageViewModel: AddMessageViewModel = viewModel(factory = AppViewModelProvider.Factory),
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    smsNavigationType: SmsNavigationType = SmsNavigationType.ADD,
    messageToUpdate: MessageDto = MessageDto(0, "", "", "Date,Time", false, false),
    navigateUp: () -> Unit
) {
    homeViewModel.updateTopBarUi(
        title = when (smsNavigationType) {
            SmsNavigationType.ADD -> ScreenTitles.ADD.title
            SmsNavigationType.UPDATE -> ScreenTitles.UPDATE.title
        },
        showNavigationIcon = true,
        navigateUp = navigateUp
    )

    var title by rememberSaveable {
        mutableStateOf(
            when (smsNavigationType) {
                SmsNavigationType.ADD -> AddMessageScreenDestination.title
                SmsNavigationType.UPDATE -> ScreenTitles.UPDATE.title
            }
        )
    }

    Column(modifier = modifier) {
        MessageInputLayout(
            messageToUpdate = messageToUpdate,
            buttonTitle = title,
            onAddMessageBtnClick = {
                when (smsNavigationType) {

                    SmsNavigationType.ADD -> {
                        homeViewModel.sendMessage(it.toMessage())
                        addMessageViewModel.addMessage(it)
                        navigateUp()
                    }

                    SmsNavigationType.UPDATE -> {
                        if ((messageToUpdate.message + messageToUpdate.delayTime) != (it.message + it.delayTime)) {
                            homeViewModel.updateMessage(
                                messageToUpdate = it.copy(id = messageToUpdate.id),
                                oldMessage = messageToUpdate
                            )
                        }
                        navigateUp()
                    }
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageInputLayout(
    messageToUpdate: MessageDto,
    buttonTitle: String,
    onAddMessageBtnClick: (MessageDto) -> Unit,
    modifier: Modifier = Modifier
) {
    var phoneNumber by rememberSaveable { mutableStateOf(messageToUpdate.phoneNumber) }
    var isValidPhoneNumber by rememberSaveable { mutableStateOf(true) }

    var message by rememberSaveable { mutableStateOf(messageToUpdate.message) }
    var isValidMessage by rememberSaveable { mutableStateOf(true) }

    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    var showTimePickerDialog by rememberSaveable { mutableStateOf(false) }

    var selectedDate by rememberSaveable { mutableStateOf(messageToUpdate.delayTime.split(",").get(0)) }
    var isSelectedDateValid by rememberSaveable { mutableStateOf(true) }
    var selectedTime by rememberSaveable { mutableStateOf(messageToUpdate.delayTime.split(",").get(1)) }
    var isSelectedTimeValid by rememberSaveable { mutableStateOf(true) }
    var isEveryday by rememberSaveable { mutableStateOf(messageToUpdate.isEveryDay) }
    var isDropdownVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .animateContentSize()
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        SmsInputField(
            value = phoneNumber,
            label = "Phone Number",
            onValueChange = {
                phoneNumber = it
                isDropdownVisible = it.isNotEmpty()
            },
            onKeyboardActionDone = {
                isDropdownVisible = false
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            placeholderId = R.string.phone_no_placeholder,
            isValid = isValidPhoneNumber,
            errorMessage = "Please enter a valid phone number!"
        )
        SmsInputField(
            value = message,
            label = "Message",
            onValueChange = { message = it },
            maxLines = 2,
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
            isEveryday = isEveryday,
            onDateButtonClick = { showDatePickerDialog = true },
            onTimeButtonClick = { showTimePickerDialog = true }
        )

        Row(
            modifier = Modifier.clickable {
                isEveryday = !isEveryday
            }
        ) {
            Checkbox(checked = isEveryday)
            Spacer(Modifier.width(8.dp))
            Text(text = "Everyday")
        }

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
                isSelectedDateValid = DateValidationUtils.validateDate(selectedDate)
                isSelectedTimeValid = DateValidationUtils.validateTime(selectedTime)

                if (isEveryday) selectedDate = DateValidationUtils.getTodayDate()

                if (isValidPhoneNumber && isValidMessage && (isEveryday || isSelectedDateValid) && isSelectedTimeValid) {
                    onAddMessageBtnClick(
                        MessageDto(
                            0,
                            phoneNumber,
                            message,
                            "$selectedDate,$selectedTime",
                            isEveryday,
                            true
                        )
                    )
                }
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = buttonTitle)
        }
    }
}

@Composable
fun PhoneNumberOptionDropdown(
    isExpanded: Boolean = false,
    phoneNumbers: List<String> = emptyList<String>(),
    onPhoneNumberClick: (String) -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
) {
    DropdownMenu(
        expanded = isExpanded,
        modifier = modifier,
        onDismissRequest = {}
    ) {
        phoneNumbers.forEach {
            DropdownMenuItem(
                text = { Text(it) },
                onClick = { onPhoneNumberClick(it) }
            )
        }
    }
}

@Composable
fun DateAndTimeInputLayout(
    date: String,
    time: String,
    isDateValid: Boolean,
    isTimeValid: Boolean,
    isEveryday: Boolean,
    onDateButtonClick: () -> Unit,
    onTimeButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (!isEveryday) {
            DateButton(
                icon = R.drawable.baseline_calendar_month_24,
                title = date,
                onButtonClick = onDateButtonClick,
                modifier = Modifier.weight(0.5f),
                isValid = isDateValid,
                errorMessage = "Choose date!"
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        DateButton(
            icon = R.drawable.baseline_access_time_filled_24,
            title = time,
            onButtonClick = onTimeButtonClick,
            modifier = Modifier
                .weight(0.5f)
                .animateContentSize(),
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
    Column(
        modifier = modifier.animateContentSize(),
    ) {
        Card(
            modifier = Modifier.height(46.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = onButtonClick
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        modifier = Modifier
                            .fillMaxHeight(),
                        painter = painterResource(icon),
                        contentDescription = ""
                    )
                    Spacer(Modifier.width(16.dp))
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
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
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
