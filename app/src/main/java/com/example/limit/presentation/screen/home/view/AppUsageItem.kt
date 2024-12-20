package com.example.limit.presentation.screen.home.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.limit.data.model.UsageStatsModel
import com.example.limit.utils.getAppIconAsBitmap
import com.example.limit.utils.getAppName

@Composable
fun AppUsageItem(usageStats: UsageStatsModel, onClick: () -> Unit) {
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },  // Add clickable modifier to trigger onClick
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            if (appIconBitmap != null) {
                Image(
                    bitmap = appIconBitmap.asImageBitmap(),
                    contentDescription = appName,
                    modifier = Modifier.size(48.dp)
                )
            } else {
                Text(
                    text = "No Icon",
                    modifier = Modifier.size(48.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = appName ?: "Unknown App",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Time Spent: ${formatTime(timeSpent)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
