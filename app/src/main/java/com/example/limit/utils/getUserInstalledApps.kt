package com.example.limit.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

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
