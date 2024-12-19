package com.example.limit.data.repository

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import com.example.limit.data.model.UsageStatsModel
import com.example.limit.utils.getUserInstalledApps

class UsageStatsRepository(private val context: Context) {

    fun getAppUsageStats(): List<UsageStatsModel> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val oneDayAgo = currentTime - 1000 * 3600 * 24
        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, oneDayAgo, currentTime)

        val userInstalledApps = getUserInstalledApps(context)

        return stats.filter { usageStats ->
            usageStats.packageName in userInstalledApps && usageStats.totalTimeInForeground > 0
        }.map {
            UsageStatsModel(it.packageName, it.totalTimeInForeground)
        }
    }
}
