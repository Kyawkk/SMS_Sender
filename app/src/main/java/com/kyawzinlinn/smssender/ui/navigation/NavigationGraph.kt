package com.kyawzinlinn.smssender.ui.navigation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kyawzinlinn.smssender.AppViewModelProvider
import com.kyawzinlinn.smssender.ui.add.AddMessageScreen
import com.kyawzinlinn.smssender.ui.add.AddMessageScreenDestination
import com.kyawzinlinn.smssender.ui.add.SmsNavigationType
import com.kyawzinlinn.smssender.ui.home.HomeScreen
import com.kyawzinlinn.smssender.ui.home.HomeScreenDestination
import com.kyawzinlinn.smssender.ui.home.PermissionRequestScreenDestination
import com.kyawzinlinn.smssender.ui.home.PermissionsRequestScreen
import com.kyawzinlinn.smssender.ui.reply.MessageReplyScreen
import com.kyawzinlinn.smssender.ui.reply.MessageReplyScreenDestination
import com.kyawzinlinn.smssender.ui.screen.HomeViewModel
import com.kyawzinlinn.smssender.utils.toMessageObject

@Composable
fun SmsNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    BackHandler(enabled = navController.currentBackStackEntry?.destination?.route == HomeScreenDestination.route,
        onBack = {
            navController.popBackStack()
        })

    NavHost(
        navController = navController,
        startDestination = HomeScreenDestination.route,
        modifier = modifier
    ) {

        composable(
            route = HomeScreenDestination.route
        ) {
            HomeScreen(homeViewModel = homeViewModel,
                navigateToAddScreen = { navigationType, message ->
                    navController.navigate(AddMessageScreenDestination.route + "/$navigationType/$message")
                })
        }

        composable(route = AddMessageScreenDestination.route + "/{navigationType}/{message}",
            enterTransition = {
                scaleIn(
                    initialScale = 0.8f,
                    animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow)
                )
            },
            exitTransition = {
                scaleOut(
                    targetScale = 0.8f, animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            }) {
            val messageString = it?.arguments!!.getString("message")!!
            val navigationType = it?.arguments!!.getString("navigationType")

            val message = messageString.toMessageObject()

            AddMessageScreen(homeViewModel = homeViewModel,
                smsNavigationType = SmsNavigationType.valueOf(navigationType!!),
                messageToUpdate = message,
                navigateUp = {
                    navController.navigate(HomeScreenDestination.route)
                })
        }

        composable(route = MessageReplyScreenDestination.route, enterTransition = {
            fadeIn() + scaleIn(
                initialScale = 0.5f, animationSpec = spring(
                    dampingRatio = 0.5f, stiffness = Spring.StiffnessLow
                )
            )
        }) {
            MessageReplyScreen(
                homeViewModel = homeViewModel
            )
        }
    }
}