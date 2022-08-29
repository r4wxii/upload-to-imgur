package com.r4wxii.uploadtoimgur

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation

@Composable
fun AppContent() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "root") {
        navigation(route = "root", startDestination = "main")
        {
            composable("main") { backStackEntry ->
                val rootEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("root")
                }
                val viewModel = hiltViewModel<MainViewModel>(rootEntry)
                MainScreen()
            }
            composable(
                "authCallback?query={query}",
                deepLinks = listOf(navDeepLink {
                    uriPattern =
                        "app://com.r4wxii.uploadtoimgur/authCallback#{query}"
                }),
                arguments = listOf(
                    navArgument("query") { nullable = true },
                ),
            ) { backStackEntry ->
                val rootEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("root")
                }
                val viewModel = hiltViewModel<MainViewModel>(rootEntry)
                Text(backStackEntry.arguments?.getString("query") ?: "")
            }
        }
    }
}