package com.kyawzinlinn.smssender.ui.reply

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text
import com.kyawzinlinn.smssender.AppViewModelProvider
import com.kyawzinlinn.smssender.R
import com.kyawzinlinn.smssender.domain.model.RepliedMessageDto
import com.kyawzinlinn.smssender.domain.model.replaceAllPhoneNumbersAndEnabledAllNumbers
import com.kyawzinlinn.smssender.ui.components.SmsInputField
import com.kyawzinlinn.smssender.ui.navigation.NavigationDestination
import com.kyawzinlinn.smssender.ui.screen.HomeViewModel
import com.kyawzinlinn.smssender.utils.ScreenTitles
import com.kyawzinlinn.smssender.utils.SmsUtils

object ReplyAddMessageScreenDestination : NavigationDestination {
    override val route: String = "Reply Add Messages"
    override val title: String = ScreenTitles.ADD_REPLY_MESSAGE.title
}

enum class ReplyNavigationType {
    Add, Update, AllNumber
}

@Composable
fun ReplyAddMessageScreen(
    homeViewModel: HomeViewModel,
    navigateUp: () -> Unit,
    navigationType: ReplyNavigationType,
    phoneNumber: String = "",
    modifier: Modifier = Modifier
) {
    homeViewModel.updateFloatingActionButtonStatus(false)
    homeViewModel.updateTopBarUi(
        title = when (navigationType) {
            ReplyNavigationType.Add -> ScreenTitles.ADD_REPLY_MESSAGE.title
            ReplyNavigationType.Update -> ScreenTitles.UPDATE_REPLY_MESSAGE.title
            ReplyNavigationType.AllNumber -> ScreenTitles.UPDATE_REPLY_MESSAGE.title
        },
        showNavigationIcon = true,
        navigateUp = navigateUp
    )

    ReplyAddMessageContent(
        modifier = modifier.fillMaxSize(),
        navigationType = navigationType,
        phoneNumber = phoneNumber,
        navigateUp = navigateUp
    )
}

@Composable
fun ReplyAddMessageContent(
    phoneNumber: String,
    navigationType: ReplyNavigationType,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    repliedMessageViewModel: RepliedMessageViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    var phoneNumber by rememberSaveable { mutableStateOf(phoneNumber) }
    var isPhoneNumberValid by rememberSaveable { mutableStateOf(true) }
    var allNumberEnabled by remember { mutableStateOf(navigationType == ReplyNavigationType.AllNumber) }
    val uiState by repliedMessageViewModel.uiState.collectAsState()
    val repliedMessages by uiState.repliedMessagesByPhoneNumber.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        if (navigationType != ReplyNavigationType.Add) repliedMessageViewModel.getPhoneNumbersByPhoneNumber(
            phoneNumber
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AnimatedVisibility(
            !allNumberEnabled
        ) {
            SmsInputField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                },
                label = "Phone Number",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                placeholderId = R.string.phone_no_placeholder,
                isValid = isPhoneNumberValid,
                errorMessage = "Please enter a valid phone number!"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    phoneNumber = ""
                    allNumberEnabled = !allNumberEnabled
                },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = allNumberEnabled,
                onCheckedChange = {
                    phoneNumber = ""
                    allNumberEnabled = it
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("All numbers")
        }

        RepliedFieldList(
            buttonTitle = if (navigationType != ReplyNavigationType.Add) "Update" else "Add",
            repliedMessages = repliedMessages,
            onAddButtonClick = { replies ->
                isPhoneNumberValid = phoneNumber.trim().isNotEmpty()

                if (isPhoneNumberValid || allNumberEnabled) {
                    repliedMessageViewModel.addRepliedMessages(
                        SmsUtils.formatPhoneNumber(phoneNumber),
                        replies.replaceAllPhoneNumbersAndEnabledAllNumbers(
                            phoneNumber,
                            allNumberEnabled
                        )
                    )

                    navigateUp()
                }
            }
        )
    }
}

@Composable
fun RepliedFieldList(
    buttonTitle: String,
    repliedMessages: List<RepliedMessageDto> = emptyList(),
    onAddButtonClick: (List<RepliedMessageDto>) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var messages by remember { mutableStateOf(repliedMessages) }
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    var initialKeyword by rememberSaveable { mutableStateOf("") }
    var initialReply by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(repliedMessages) {
        messages = repliedMessages
    }

    fun removeMessage(messageToRemove: RepliedMessageDto) {
        messages = messages.filterNot { it == messageToRemove }
    }

    fun updateMessage(indexToUpdate: Int, keyword: String, reply: String) {
        messages = messages.mapIndexed { index, message ->
            if (index == indexToUpdate) {
                message.copy(
                    incomingMessage = keyword,
                    repliedMessage = reply
                ) // Update the message
            } else {
                message
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reply Field", style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = {
                initialReply = ""
                initialKeyword = ""

                showDialog = true
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }

        if (showDialog) {
            AddRepliedMessageDialog(
                initialKeyword = initialKeyword,
                initialReply = initialReply,
                indexToUpdate = selectedIndex,
                onDismissRequest = {
                    showDialog = false
                },
                onReplyAdded = { indexToUpdate, keyword, reply, isUpdateMode ->
                    if (isUpdateMode) updateMessage(indexToUpdate, keyword, reply)
                    else messages = messages + RepliedMessageDto(0, "", keyword, reply, false)
                }
            )
        }

        // if reply field is not empty, show keyword  ->  reply title layout
        if (messages.isNotEmpty()) ReplyFieldTitleLayout()

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            messages.forEachIndexed { index, repliedMessageDto ->
                RepliedMessageListItem(
                    repliedMessageDto = repliedMessageDto,
                    onDeleteClick = {
                        removeMessage(repliedMessageDto)
                    },
                    onEditClick = {
                        initialReply = repliedMessageDto.repliedMessage
                        initialKeyword = repliedMessageDto.incomingMessage
                        selectedIndex = index
                        showDialog = true
                    }
                )
            }
        }

        FilledTonalButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAddButtonClick(messages)
            }) {
            Text(buttonTitle)
        }
    }
}

@Composable
fun ReplyFieldTitleLayout(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Keyword",
            modifier = Modifier
                .weight(0.4f)
                .padding(start = 8.dp)
        )
        Text(
            text = "Reply",
            modifier = Modifier
                .weight(0.4f)
                .padding(start = 8.dp)
        )
        Spacer(Modifier.weight(0.1f))
        Spacer(Modifier.weight(0.1f))
    }
}

@Composable
fun AddRepliedMessageDialog(
    onDismissRequest: () -> Unit,
    indexToUpdate: Int,
    onReplyAdded: (Int, String, String, Boolean) -> Unit,
    initialKeyword: String = "",
    initialReply: String = "",
    modifier: Modifier = Modifier
) {
    var keyword by rememberSaveable { mutableStateOf(initialKeyword) }
    var isKeywordValid by rememberSaveable { mutableStateOf(true) }
    var reply by rememberSaveable { mutableStateOf(initialReply) }
    var isReplyValid by rememberSaveable { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.Default.AddBox,
                contentDescription = null
            )
        },
        title = {
            Text(
                "Add reply field",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = {
                isKeywordValid = keyword.isNotEmpty()
                isReplyValid = reply.isNotEmpty()
                if (isKeywordValid && isReplyValid) {
                    onReplyAdded(
                        indexToUpdate,
                        keyword,
                        reply,
                        (initialReply.isNotEmpty() && initialKeyword.isNotEmpty())
                    )
                    onDismissRequest()
                }
            }) {
                Text("Done")
            }
        },
        text = {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SmsInputField(
                    value = keyword,
                    label = "Keyword",
                    errorMessage = "Keyword must not be empty!",
                    onValueChange = {
                        keyword = it
                    },
                    placeholderId = R.string.keyword,
                    isValid = isKeywordValid
                )

                SmsInputField(
                    value = reply,
                    errorMessage = "Reply must not be empty!",
                    onValueChange = {
                        reply = it
                    },
                    placeholderId = R.string.reply,
                    label = "Reply",
                    isValid = isReplyValid
                )
            }
        }
    )
}

@Composable
fun RepliedMessageListItem(
    modifier: Modifier = Modifier,
    onEditClick: (RepliedMessageDto) -> Unit,
    onDeleteClick: (RepliedMessageDto) -> Unit,
    repliedMessageDto: RepliedMessageDto = RepliedMessageDto(0, "09123456789", "Hello", "Ok", false)
) {
    androidx.compose.material3.Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = repliedMessageDto.incomingMessage,
                modifier = Modifier
                    .weight(0.4f)
                    .padding(end = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = repliedMessageDto.repliedMessage,
                modifier = Modifier
                    .weight(0.4f)
                    .padding(start = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(
                modifier = Modifier.weight(0.1f),
                onClick = {
                    onEditClick(repliedMessageDto)
                }
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(
                modifier = Modifier.weight(0.1f),
                onClick = {
                    onDeleteClick(repliedMessageDto)
                }
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}