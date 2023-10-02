package com.kyawzinlinn.smssender.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    BackHandler(
        enabled = navController.currentBackStackEntry?.destination?.route == HomeScreenDestination.route,
        onBack = {
            navController.popBackStack()
        }
    )

    NavHost(
        navController = navController,
        startDestination = HomeScreenDestination.route,
        modifier = modifier
    ) {

        composable(
            route = HomeScreenDestination.route
        ) {
            HomeScreen(navigateToAddScreen = { navController.navigate(AddMessageScreenDestination.route) })
        }

        composable(
            route = AddMessageScreenDestination.route,
            enterTransition = {
                scaleIn(
                    initialScale = 0.8f,
                    animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow)
                )
            },
            exitTransition = {
                scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
            AddMessageScreen(navigateUp = {
                navController.navigate(HomeScreenDestination.route)
            })
        }
    }
}