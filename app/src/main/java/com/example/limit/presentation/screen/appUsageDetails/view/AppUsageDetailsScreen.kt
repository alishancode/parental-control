package com.example.limit.presentation.screen.home.view

import android.app.usage.UsageStats
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.limit.data.model.UsageStatsModel
import com.example.limit.utils.getAppIconAsBitmap
import com.example.limit.utils.getAppName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUsageDetailsScreen(usageStats: UsageStats, navController: NavController) {
    val context = LocalContext.current
    val appName = getAppName(context, usageStats.packageName)
    val appIconBitmap = getAppIconAsBitmap(context, usageStats.packageName)
    val timeSpent = usageStats.totalTimeInForeground / 1000 // Convert ms to seconds

    // Format time as hours, minutes, and seconds
    fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    val timeSpentFormatted = formatTime(timeSpent)
    val appCategory = "Social Media" // Example category

    // State for filter selection (daily or weekly)
    var isDailyFilter by remember { mutableStateOf(true) }

    val onBackPressed:()->Unit = {
        navController.navigate("home")

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Back Navigation Icon with functionality
        IconButton(onClick = { onBackPressed() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Header Section: App Name and Icon
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            if (appIconBitmap != null) {
                Image(
                    bitmap = appIconBitmap.asImageBitmap(),
                    contentDescription = appName,
                    modifier = Modifier.size(64.dp)
                )
            } else {
                Text(
                    text = "No Icon",
                    modifier = Modifier.size(64.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = appName ?: "Unknown App",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Time Spent: $timeSpentFormatted",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Usage Category Section
        Text(
            text = "Category: $appCategory",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filter Section for Daily/Weekly Usage Data with Icons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { isDailyFilter = true }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Daily")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Daily", color = if (isDailyFilter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                }
            }
            TextButton(onClick = { isDailyFilter = false }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Weekly")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Weekly", color = if (!isDailyFilter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Time Breakdown Section with Cards for Better Visibility based on filter selection
        Text(
            text = "Usage Breakdown",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primaryContainer
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TimeBreakdownItem(label = if (isDailyFilter) "Today" else "This Week", timeSpentFormatted)
                TimeBreakdownItem(label = if (isDailyFilter) "Yesterday" else "Last Week", "12:45:30") // Placeholder for dynamic time here based on filter.
                TimeBreakdownItem(label="Total", if (isDailyFilter) "20:10:30" else "80:10:30") // Placeholder for total time.
            }
        }

        Spacer(modifier=Modifier.height(16.dp))

        // App Limits Section with Improved UI Elements
        AppLimitSection(context)

        Spacer(modifier=Modifier.height(16.dp))

        // App Blocking Section with Improved UI Elements
        AppBlockSection(context)

        Spacer(modifier=Modifier.height(16.dp))

        // Manage Notifications Section with Improved UI Elements
        ManageNotificationsSection(context)
    }
}

@Composable
fun TimeBreakdownItem(label: String, timeSpent: String) {
    Row(
        modifier=Modifier.fillMaxWidth(),
        horizontalArrangement=Arrangement.SpaceBetween,
    ) {
        Text(label, style=MaterialTheme.typography.bodyMedium)
        Text(timeSpent, style=MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun AppLimitSection(context: Context) {
    Card(
        modifier=Modifier.fillMaxWidth(),
        elevation=CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier=Modifier.padding(16.dp)) {
            Text(
                text="App Limit",
                style=MaterialTheme.typography.bodyLarge,
                fontWeight=FontWeight.Bold,
                color=MaterialTheme.colorScheme.primaryContainer
            )
            Text(
                text="Currently: No Limit Set", // Show limit status dynamically if needed.
                style=MaterialTheme.typography.bodyMedium,
                color=MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier=Modifier.height(8.dp))

            Button(onClick={
                context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }) {
                Text("Set App Limit")
            }
        }
    }
}

@Composable
fun AppBlockSection(context: Context) {
    Card(
        modifier=Modifier.fillMaxWidth(),
        elevation=CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier=Modifier.padding(16.dp)) {
            Text(
                text="App Block",
                style=MaterialTheme.typography.bodyLarge,
                fontWeight=FontWeight.Bold,
                color=MaterialTheme.colorScheme.primaryContainer
            )
            Text(
                text="Currently: Blocked during certain times", // Dynamic block status.
                style=MaterialTheme.typography.bodyMedium,
                color=MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier=Modifier.height(8.dp))

            Button(onClick={
                context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }) {
                Text("Manage Blocks")
            }
        }
    }
}

@Composable
fun ManageNotificationsSection(context: Context) {
    Card(
        modifier=Modifier.fillMaxWidth(),
        elevation=CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier=Modifier.padding(16.dp)) {
            Text(
                text="Notifications",
                style=MaterialTheme.typography.bodyLarge,
                fontWeight=FontWeight.Bold,
                color=MaterialTheme.colorScheme.primaryContainer
            )

            Text(
                text="Notifications: On", // Change based on status dynamically.
                style=MaterialTheme.typography.bodyMedium,
                color=MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier=Modifier.height(8.dp))

            Button(onClick={
                context.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
            }) {
                Text("Manage Notifications")
            }
        }
    }
}
