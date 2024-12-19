package com.example.limit.presentation.navigation

import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.limit.data.model.BottomNavigationItem

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationItem>,
    navController: NavHostController
) {
    NavigationBar {
        items.forEach { item ->
            val isSelected = navController.currentDestination?.route == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    // Navigate to the selected screen
                    navController.navigate(item.route) {
                        // Avoid stack duplication (optional)
                        launchSingleTop = true
                    }
                },
                icon = {
                    // Show badge if badgeCount is not null
                    if (item.badgeCount != null && item.badgeCount > 0) {
                        Badge { Text(item.badgeCount.toString()) }
                    }
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unSelectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) }
            )
        }
    }
}
