@file:OptIn(ExperimentalMaterial3Api::class)

package com.kyawzinlinn.smssender.ui.reply

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kyawzinlinn.smssender.data.model.RepliedMessageDto

@Composable
fun ReplyListItem(
    phoneNumber: String,
    replies: List<RepliedMessageDto>,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier,
        onClick = onItemClick,
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (phoneNumber.isEmpty()) "All numbers" else phoneNumber,
                        style = MaterialTheme.typography.titleMedium
                    )
                    AnimatedVisibility(!expanded) {
                        Text(
                            text = "${replies.size} Replies",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }

            if (expanded) RepliesLayout(
                replies = replies
            )
        }
    }
}

@Composable
fun RepliesLayout(
    replies: List<RepliedMessageDto>,
    modifier: Modifier = Modifier
) {
    Spacer(Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Keyword")
        Text("Reply")
    }

    Spacer(Modifier.height(8.dp))

    Column(
        modifier = modifier
    ) {
        replies.forEachIndexed{ index, repliedMessageDto ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                Text(
                    text = "${index + 1}. ${repliedMessageDto.incomingMessage}",
                    style = MaterialTheme.typography.labelMedium,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = repliedMessageDto.repliedMessage,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Left
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
        }
    }
}