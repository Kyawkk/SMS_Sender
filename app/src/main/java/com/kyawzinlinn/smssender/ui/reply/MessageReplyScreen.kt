package com.kyawzinlinn.smssender.ui.reply

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text
import com.kyawzinlinn.smssender.AppViewModelProvider
import com.kyawzinlinn.smssender.R
import com.kyawzinlinn.smssender.domain.model.RepliedMessageDto
import com.kyawzinlinn.smssender.domain.model.toRepliedMessage
import com.kyawzinlinn.smssender.ui.components.NoMessagesLayout
import com.kyawzinlinn.smssender.ui.navigation.NavigationDestination
import com.kyawzinlinn.smssender.ui.screen.HomeViewModel
import com.kyawzinlinn.smssender.utils.ScreenTitles

object MessageReplyScreenDestination : NavigationDestination {
    override val route: String = "Reply Messages"
    override val title: String = ScreenTitles.REPLY.title
}

@Composable
fun MessageReplyScreen(
    homeViewModel: HomeViewModel,
    onReplyItemClick: (String) -> Unit,
    repliedMessageViewModel: RepliedMessageViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    homeViewModel.updateTopBarUi(MessageReplyScreenDestination.title, false)

    val uiState by repliedMessageViewModel.uiState.collectAsState()
    val messages by uiState.repliedMessages.collectAsState(initial = emptyList())
    var showEmptyMessage by rememberSaveable { mutableStateOf(messages.isEmpty()) }

    LaunchedEffect(messages) {
        showEmptyMessage = messages.isEmpty()
    }

    Column(
        modifier = Modifier.fillMaxSize().animateContentSize()
    ) {

        if (!showEmptyMessage) {
            RepliedMessageContentList(
                messages.toRepliedMessage(),
                modifier = modifier.fillMaxSize(),
                onReplyItemClick = onReplyItemClick
            )
        } else {
            NoMessagesLayout(
                titleId = R.string.no_reply_messages,
                descriptionId = R.string.no_reply_messages_description
            )
        }
    }
}

@Composable
fun RepliedMessageContentList(
    repliedMessages: Map<String, List<RepliedMessageDto>>,
    onReplyItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(repliedMessages.entries.toList()) { (phoneNumber, replies) ->
            ReplyListItem(phoneNumber = phoneNumber, replies = replies, onItemClick = {
                onReplyItemClick(if (phoneNumber.isEmpty()) " " else phoneNumber)
            })
        }
    }
}