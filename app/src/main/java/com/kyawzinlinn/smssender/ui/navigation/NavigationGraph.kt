package com.kyawzinlinn.smssender.ui.navigation

import android.util.Log
import androidx.activity.compose.BackHandler
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
import com.kyawzinlinn.smssender.ui.reply.MessageReplyScreen
import com.kyawzinlinn.smssender.ui.reply.MessageReplyScreenDestination
import com.kyawzinlinn.smssender.ui.reply.ReplyAddMessageScreen
import com.kyawzinlinn.smssender.ui.reply.ReplyAddMessageScreenDestination
import com.kyawzinlinn.smssender.ui.reply.ReplyNavigationType
import com.kyawzinlinn.smssender.ui.screen.HomeViewModel
import com.kyawzinlinn.smssender.utils.Transition
import com.kyawzinlinn.smssender.utils.toMessageObject

@Composable
fun SmsNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
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
            homeViewModel.apply {
                updateFloatingActionButtonStatus(true)
                updateBottomAppBarStatus(true)
            }

            HomeScreen(
                homeViewModel = homeViewModel,
                navigateToAddScreen = { navigationType, message ->
                    navController.navigate(AddMessageScreenDestination.route + "/$navigationType/$message")
                }
            )
        }

        composable(
            route = AddMessageScreenDestination.route + "/{navigationType}/{message}",
            enterTransition = { Transition.enter },
            exitTransition = { Transition.exit }
        ) {

            homeViewModel.apply {
                updateFloatingActionButtonStatus(false)
                updateBottomAppBarStatus(false)
            }

            val messageString = it?.arguments!!.getString("message")!!
            val navigationType = it?.arguments!!.getString("navigationType")
            val message = messageString.toMessageObject()

            AddMessageScreen(
                homeViewModel = homeViewModel,
                smsNavigationType = SmsNavigationType.valueOf(navigationType!!),
                messageToUpdate = message,
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = MessageReplyScreenDestination.route,
            enterTransition = { Transition.enter },
            exitTransition = { Transition.exit }
        ) {
            homeViewModel.apply {
                updateFloatingActionButtonStatus(true)
                updateBottomAppBarStatus(true)
            }
            MessageReplyScreen(
                homeViewModel = homeViewModel,
                onReplyItemClick = { phoneNumber ->
                    val navigationType = if (phoneNumber.trim().isEmpty()) ReplyNavigationType.AllNumber else ReplyNavigationType.Update
                    Log.d("TAG", "isAllNumberS: ${phoneNumber.trim().isEmpty()}  $phoneNumber")

                    navController.navigate(
                        ReplyAddMessageScreenDestination.route + "/$phoneNumber/${navigationType}"
                    )
                }
            )
        }

        composable(
            route = ReplyAddMessageScreenDestination.route + "/{phoneNumber}/{navigationType}",
            enterTransition = { Transition.enter },
            exitTransition = { Transition.exit }
        ) {
            homeViewModel.apply {
                updateFloatingActionButtonStatus(true)
                updateBottomAppBarStatus(false)
            }

            val phoneNumber = it?.arguments?.getString("phoneNumber")!!
            val navigationType = it?.arguments?.getString("navigationType")!!

            ReplyAddMessageScreen(
                homeViewModel = homeViewModel,
                phoneNumber = phoneNumber.trim(),
                navigationType = ReplyNavigationType.valueOf(navigationType),
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}