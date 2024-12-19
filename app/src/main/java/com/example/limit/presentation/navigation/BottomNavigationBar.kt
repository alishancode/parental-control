package com.example.limit.presentation.navigation

import androidx.compose.material3.Badge
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
                onClick = { navController.navigate(item.route) },
                icon = {
                    if (item.badgeCount != null) {
//                        Badge { Text(item.badgeCount.toString()) }
                    }
                    androidx.compose.material3.Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unSelectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) }
            )
        }
    }
}