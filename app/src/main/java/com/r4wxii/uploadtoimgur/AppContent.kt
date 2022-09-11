package com.r4wxii.uploadtoimgur

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
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
                val query = backStackEntry.arguments?.getString("query")
                val lifecycleOwner = LocalLifecycleOwner.current
                val requiresTokenFlow = remember(lifecycleOwner) {
                    viewModel.requiresToken.flowWithLifecycle(
                        lifecycleOwner.lifecycle,
                        Lifecycle.State.STARTED,
                    )
                }

                LaunchedEffect(lifecycleOwner) {
                    requiresTokenFlow.collect { requiredAuthorize ->
                        if (query.isNullOrBlank()) {
                            return@collect
                        }

                        if (requiredAuthorize) {
                            viewModel.setRefreshToken(query)
                        } else {
                            navController.popBackStack()
                        }
                    }
                }

                CircularProgressIndicator()
            }
        }
    }
}