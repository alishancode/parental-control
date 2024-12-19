package com.example.limit.utils

import android.content.Context
import android.app.usage.UsageStatsManager

// Utility function to check if usage stats permission is granted
fun isUsageStatsPermissionGranted(context: Context): Boolean {
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val stats = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        System.currentTimeMillis() - 1000 * 3600 * 24,
        System.currentTimeMillis()
    )
    return stats.isNotEmpty() // Returns true if permission is granted, false otherwise
}
