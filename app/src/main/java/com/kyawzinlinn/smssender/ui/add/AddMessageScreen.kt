@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.kyawzinlinn.smssender.ui.add

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object AddMessageScreenDestination : NavigationDestination {
    override val route: String = "ADD"
    override val title: String = "Add Message"
}

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
    var phoneNumber by rememberSaveable {
        mutableStateOf("")
    }
    var message by rememberSaveable {
        mutableStateOf("")
    }
    var selectedDateAndTime by rememberSaveable { mutableStateOf(LocalDateTime.now().plusDays(1)) }

    var showDateTimeDialog by rememberSaveable { mutableStateOf(false) }

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
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
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
        
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = MaterialTheme.shapes.small,
            onClick = {showDateTimeDialog = true}
        ) {
            Box (
                contentAlignment = Alignment.Center
            ){
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(12.dp),
                        painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                        contentDescription = ""
                    )
                    Text(
                        text = selectedDateAndTime.toFormattedDateTime()
                    )
                }
            }
        }

        if (showDateTimeDialog) DateTimeSelectionDialog() {
            showDateTimeDialog = false
            selectedDateAndTime = it
        }

        Button(
            onClick = {
                onAddMessageBtnClick(
                    MessageDTO(0, phoneNumber, message, selectedDateAndTime.toFormattedDateTime(), false)
                )
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.add_message))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimeSelectionDialog(
    onClose: (LocalDateTime?) -> Unit
) {
    val selectedDateTime = remember {
        mutableStateOf<LocalDateTime?>(
            LocalDateTime.now()
        )
    }

    DateTimeDialog(
        state = rememberUseCaseState(visible = true, onCloseRequest = {onClose(selectedDateTime.value)}),
        selection = DateTimeSelection.DateTime(
            timeFormatStyle = FormatStyle.FULL
        ) { newDateTime ->
            selectedDateTime.value = newDateTime
            Log.d("TAG", "DateTimeSelectionDialog: ${newDateTime.toLocalTime()}")
        },
    )
}