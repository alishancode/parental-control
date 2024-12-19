package com.example.limit.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.limit.ChatScreen
import com.example.limit.SettingsScreen
import com.example.limit.presentation.screen.home.view.HomeScreen


@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") { HomeScreen() }
        composable("chat") { ChatScreen() }
        composable("settings") { SettingsScreen() }
    }
}