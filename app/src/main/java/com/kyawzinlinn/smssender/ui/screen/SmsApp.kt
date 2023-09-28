@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)

package com.kyawzinlinn.smssender.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kyawzinlinn.smssender.ui.navigation.SmsNavHost

@Composable
fun SmsApp(
    navController: NavHostController = rememberNavController()
) {
    SmsNavHost(navController = navController)
}

enum class NavigateIconType {
    NAVIGATE_BACK, ADD_SCREEN
}

@Composable
fun SmsAppTopBar(
    title: String,
    showNavigateIcon: Boolean,
    navigateIconType: NavigateIconType =  NavigateIconType.NAVIGATE_BACK,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            if (showNavigateIcon){
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = when (navigateIconType) {
                            NavigateIconType.NAVIGATE_BACK -> Icons.Default.ArrowBack
                            NavigateIconType.ADD_SCREEN -> Icons.Default.Clear
                        },
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SmsAppTopBarPreview() {
    SmsAppTopBar(
        title = "Home",
        showNavigateIcon = true,
        navigateIconType = NavigateIconType.NAVIGATE_BACK
    ) {

    }
}