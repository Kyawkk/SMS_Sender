package com.kyawzinlinn.smssender.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kyawzinlinn.smssender.ui.add.AddMessageScreen
import com.kyawzinlinn.smssender.ui.add.AddMessageScreenDestination
import com.kyawzinlinn.smssender.ui.home.HomeScreen
import com.kyawzinlinn.smssender.ui.home.HomeScreenDestination

@Composable
fun SmsNavHost(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeScreenDestination.route,
        modifier = modifier
    ) {
        composable(
            route = HomeScreenDestination.route,
        ) {
            HomeScreen(navigateToAddScreen = { navController.navigate(AddMessageScreenDestination.route) })
        }

        composable(route = AddMessageScreenDestination.route, enterTransition = {
            scaleIn(
                animationSpec = tween(
                    300, easing = FastOutLinearInEasing
                ), initialScale = 0.8f
            ) + fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                ),
                initialAlpha = 0f
            )
        }, exitTransition = {
            scaleOut(
                animationSpec = tween(
                    300, easing = FastOutLinearInEasing
                ), targetScale = 0.8f
            ) + fadeOut(
                animationSpec = tween(
                    300, easing = FastOutLinearInEasing
                ), targetAlpha = 0f
            )
        }) {
            AddMessageScreen(navigateUp = { navController.navigate(HomeScreenDestination.route) })
        }
    }
}