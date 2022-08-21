package com.r4wxii.uploadtoimgur

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

@Composable
fun AppContent() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen() }
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
            Text(backStackEntry.arguments?.getString("query") ?: "")
        }
    }
}