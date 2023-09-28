package com.kyawzinlinn.smssender.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
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
    NavHost(navController = navController, startDestination = HomeScreenDestination.route, modifier = modifier) {
        composable(route = HomeScreenDestination.route){
            HomeScreen(navigateToAddScreen = {navController.navigate(AddMessageScreenDestination.route)})
        }

        composable(route = AddMessageScreenDestination.route){
            AddMessageScreen(navigateUp = {navController.navigate(HomeScreenDestination.route)})
        }
    }
}