@file:OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)

package com.kyawzinlinn.smssender.ui.screen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kyawzinlinn.smssender.AppViewModelProvider
import com.kyawzinlinn.smssender.model.MessageDTO
import com.kyawzinlinn.smssender.ui.add.AddMessageScreenDestination
import com.kyawzinlinn.smssender.ui.add.SmsNavigationType
import com.kyawzinlinn.smssender.ui.home.HomeScreenDestination
import com.kyawzinlinn.smssender.ui.navigation.SmsNavHost
import com.kyawzinlinn.smssender.ui.reply.MessageReplyScreenDestination
import com.kyawzinlinn.smssender.ui.theme.productSansFontFamily
import com.kyawzinlinn.smssender.utils.ScreenTitles

@Composable
fun SmsApp(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFloatingActionButton by rememberSaveable { mutableStateOf(false) }
    var showNavigateIcon by rememberSaveable { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf(HomeScreenDestination.title) }

    LaunchedEffect(uiState.showFloatingActionButton, uiState.showNavigationIcon, uiState.title) {
        showFloatingActionButton = uiState.showFloatingActionButton
        showNavigateIcon = uiState.showNavigationIcon
        title = uiState.title
    }

    Scaffold(topBar = {
        SmsAppTopBar(
            title = title, showNavigateIcon = showNavigateIcon, navigateUp = uiState.navigateUp
        )
    }, floatingActionButton = {
        SmsFloatingActionButton(showFloatingActionButton,
            navigateToAddScreen = { type, messageDto ->
                navController.navigate(AddMessageScreenDestination.route + "/$type/$messageDto")
            })
    }, bottomBar = {
        AnimatedVisibility(
            visible = !showNavigateIcon,
            enter = fadeIn() + slideInVertically(animationSpec = tween(400)),
            exit = fadeOut() + slideOutVertically(animationSpec = tween(400))
        ) {
            SmsAppBottomNavigation(
                navController = navController,
                onNavigationItemSelected = { screen ->
                    when (screen) {
                        ScreenTitles.HOME.title -> {
                            navController.navigate(HomeScreenDestination.route)
                        }

                        ScreenTitles.REPLY.title -> {
                            navController.navigate(MessageReplyScreenDestination.route)
                        }
                    }
                })
        }
    }) {
        SmsNavHost(
            navController = navController, modifier = Modifier.padding(it)
        )
    }
}

enum class NavigateIconType {
    NAVIGATE_BACK, ADD_SCREEN
}

@Composable
fun SmsFloatingActionButton(
    visible: Boolean, navigateToAddScreen: (SmsNavigationType, MessageDTO) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(animationSpec = tween(300), initialScale = 0.8f) + fadeIn(),
        exit = scaleOut(animationSpec = tween(300), targetScale = 0.8f) + fadeOut()
    ) {
        FloatingActionButton(onClick = {
            navigateToAddScreen(
                SmsNavigationType.ADD, MessageDTO(0, "", "", "", false, false)
            )
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
        }
    }
}

@Composable
fun SmsAppTopBar(
    title: String,
    showNavigateIcon: Boolean,
    navigateIconType: NavigateIconType = NavigateIconType.NAVIGATE_BACK,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(title = {
        AnimatedContent(targetState = title, transitionSpec = {
            fadeIn() + slideInVertically(
                animationSpec = tween(400),
                initialOffsetY = { it }) togetherWith fadeOut(
                animationSpec = tween(
                    200
                )
            ) + slideOutVertically {
                -it
            }
        }) { newTitle ->
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = newTitle,
                    fontFamily = productSansFontFamily,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }, colors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    ), navigationIcon = {
        if (showNavigateIcon) {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = when (navigateIconType) {
                        NavigateIconType.NAVIGATE_BACK -> Icons.Default.ArrowBack
                        NavigateIconType.ADD_SCREEN -> Icons.Default.Clear
                    }, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    })
}

@Composable
fun SmsAppBottomNavigation(
    navController: NavHostController,
    onNavigationItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var currentRoute by rememberSaveable { mutableStateOf(HomeScreenDestination.route) }

    LaunchedEffect(navBackStackEntry?.destination?.route) {
        currentRoute = navBackStackEntry?.destination?.route!!
    }

    val bottomNavigationItems = listOf(
        SmsBottomNavigationItem(
            HomeScreenDestination.route, Icons.Outlined.Home, Icons.Filled.Home
        ),
        SmsBottomNavigationItem(
            MessageReplyScreenDestination.route, Icons.Outlined.Sms, Icons.Filled.Sms
        ),
    )

    NavigationBar(
        modifier = modifier
    ) {
        bottomNavigationItems.forEach { navItem ->
            NavigationBarItem(icon = {
                Icon(
                    imageVector = if (currentRoute != navItem.title) navItem.selectedIcon else navItem.unSelectedIcon,
                    contentDescription = null
                )
            }, selected = currentRoute == navItem.title, onClick = {
                currentRoute = navItem.title
                onNavigationItemSelected(navItem.title)
            })
        }
    }
}

data class SmsBottomNavigationItem(
    val title: String, val selectedIcon: ImageVector, val unSelectedIcon: ImageVector
)