package com.example.limit.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.provider.Settings
import android.content.Intent
import android.widget.Toast


fun getUserInstalledApps(context: Context): List<String> {
    val pm = context.packageManager
    // Get all installed applications
    return pm.getInstalledApplications(PackageManager.GET_META_DATA)
        .filter { appInfo ->
            // Check if the app is a system app or not
            val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            // Check if the app has a launchable intent (i.e., can be opened by the user)
            val hasLaunchIntent = pm.getLaunchIntentForPackage(appInfo.packageName) != null
            // Include user apps and launchable system apps
            !isSystemApp || hasLaunchIntent
        }
        .map { it.packageName } // Extract package names
}



fun getUsageStatsForPackage(context: Context, packageName: String): UsageStats? {
    // Check if usage access permission is granted
    if (!hasUsageStatsPermission(context)) {
        // If permission is not granted, show a prompt to request the user to enable it
        promptForUsageStatsPermission(context)
        return null
    }

    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    // Get the current time (or a time range for stats)
    val currentTime = System.currentTimeMillis()
    val oneDayAgo = currentTime - (1000 * 60 * 60 * 24) // One day ago

    // Query usage stats for the last 24 hours
    val usageStatsList = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY, oneDayAgo, currentTime
    )

    // Iterate through the list of usage stats and find the one matching the package name
    for (usageStats in usageStatsList) {
        if (usageStats.packageName == packageName) {
            return usageStats
        }
    }
    return null // No stats found for this package
}

fun hasUsageStatsPermission(context: Context): Boolean {
    // Check if the app has the permission to access usage stats
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = appOps.checkOpNoThrow(
            android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == android.app.AppOpsManager.MODE_ALLOWED
    }
    return false
}

fun promptForUsageStatsPermission(context: Context) {
    // If the app doesn't have permission, show a Toast or direct the user to the settings page
    Toast.makeText(context, "Please enable Usage Access Permission", Toast.LENGTH_LONG).show()

    // Intent to open the Usage Access Settings page
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    context.startActivity(intent)
}


