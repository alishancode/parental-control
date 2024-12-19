package com.example.limit.presentation.screen.home.view

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.limit.presentation.screen.home.viewmodel.HomeScreenViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.limit.presentation.screen.home.viewmodel.HomeScreenViewModelFactory
import com.example.limit.utils.isUsageStatsPermissionGranted



@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
//    val navController = rememberNavController()


    val viewModel: HomeScreenViewModel = viewModel(
        factory = HomeScreenViewModelFactory(context)
    )




    val appUsageStats = viewModel.appUsageStats.value


    LaunchedEffect(Unit) {
        if (!isUsageStatsPermissionGranted(context)) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            context.startActivity(intent)
        }else{
            viewModel.checkAndLoadAppUsageStats(context)
        }
    }





    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "App Usage Stats",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (appUsageStats.isEmpty()) {
            Text(
                text = "No usage stats available. Ensure permission is granted.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                context.startActivity(intent)
            }) {
                Text("Grant Permission")
            }
        } else {
            appUsageStats.forEach { usageStats ->
                AppUsageItem(usageStats = usageStats, onClick = {
                    // Safely navigate to the app usage details screen with the package name
                    navController.navigate("app_usage_details/${usageStats.packageName}")
                })
            }

        }
    }
}
