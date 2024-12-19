package com.example.limit.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.limit.data.model.BottomNavigationItem
import com.example.limit.presentation.navigation.AppNavGraph
import com.example.limit.presentation.navigation.BottomNavigationBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Define navigation items in a function for better readability
    val items = getBottomNavigationItems()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = items,
                navController = navController
            )
        }
    ) { innerPadding ->
        // Pass innerPadding to AppNavGraph to avoid UI overlap
        AppNavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

// Function to generate BottomNavigationItem list
private fun getBottomNavigationItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            title = "Home",
            route = "home",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Chat",
            route = "chat",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home,
            hasNews = false,
            badgeCount = 45
        ),
        BottomNavigationItem(
            title = "Settings",
            route = "settings",
            selectedIcon = Icons.Filled.Settings,
            unSelectedIcon = Icons.Outlined.Settings,
            hasNews = true
        )
    )
}
