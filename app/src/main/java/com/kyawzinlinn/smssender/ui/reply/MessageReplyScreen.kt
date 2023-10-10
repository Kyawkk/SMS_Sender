package com.kyawzinlinn.smssender.ui.reply

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text
import com.kyawzinlinn.smssender.AppViewModelProvider
import com.kyawzinlinn.smssender.ui.navigation.NavigationDestination
import com.kyawzinlinn.smssender.ui.screen.HomeViewModel
import com.kyawzinlinn.smssender.utils.ScreenTitles

object MessageReplyScreenDestination: NavigationDestination{
    override val route: String = "Reply"
    override val title: String = ScreenTitles.REPLY.title

}

@Composable
fun MessageReplyScreen(
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    homeViewModel.updateTopBarUi(MessageReplyScreenDestination.title,false)
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Message Reply")
    }
}