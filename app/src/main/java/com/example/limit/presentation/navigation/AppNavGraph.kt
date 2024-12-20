package com.example.limit.presentation.navigation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.limit.ChatScreen
import com.example.limit.SettingsScreen
import com.example.limit.presentation.screen.home.view.AppUsageDetailsScreen
import com.example.limit.presentation.screen.home.view.HomeScreen
import com.example.limit.utils.getUsageStatsForPackage
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") { HomeScreen(navController = navController) }

        composable("app_usage_details/{packageName}") { backStackEntry ->
            val packageName = backStackEntry.arguments?.getString("packageName")
            val context = LocalContext.current // Obtain context here

            if (packageName != null) {
                // Log the packageName to verify it's passed correctly
                Log.d("AppNavGraph", "Navigating to app usage details for package: $packageName")

                // Fetch usage stats for the app
                val usageStats = getUsageStatsForPackage(context, packageName)

                if (usageStats != null) {

                    AppUsageDetailsScreen(usageStats = usageStats, navController)
                } else {
                    // Log and display an error if usageStats is null
                    Log.e("AppNavGraph", "Usage stats not found for package: $packageName")
                    // Optionally show a fallback screen or message
                }
            } else {
                Log.e("AppNavGraph", "Package name is null")
            }
        }

        composable("chat") { ChatScreen() }
        composable("settings") { SettingsScreen() }
    }
}
