package com.example.limit.managers

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.util.Log

class AppUsageTracker(private val context: Context) {

    private val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    fun getForegroundAppPackage(): String? {
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 24 * 60 * 60 * 1000 // Last 24 hours
        val usageStats: List<UsageStats> = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        )

        val recentAppUsage = usageStats.maxByOrNull { it.lastTimeUsed }
        return recentAppUsage?.packageName
    }

    fun getAppUsageTimeInSeconds(packageName: String): Int {
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 24 * 60 * 60 * 1000 // Last 24 hours
        val usageStats: List<UsageStats> = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        )

        val appUsage = usageStats.find { it.packageName == packageName }
        return ((appUsage?.totalTimeInForeground ?: 0) / 1000).toInt() // Convert to seconds and ensure it's an Int
    }
}
