package com.example.limit.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.example.limit.data.model.AppLimit
import com.example.limit.managers.AppUsageTracker
import com.example.limit.managers.NotificationManager
import com.example.limit.managers.OverlayManager
import com.example.limit.managers.AudioManager

class AppLimitService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var appUsageTracker: AppUsageTracker
    private lateinit var notificationManager: NotificationManager
    private lateinit var overlayManager: OverlayManager
    private lateinit var audioManager: AudioManager

    private val appLimits = listOf(
        AppLimit("com.google.android.youtube", 15) // Limit YouTube usage to 15 seconds
    )

    private val appUsageMap = mutableMapOf<String, Long>()
    private var currentApp: String? = null
    private var lastCheckedTime = System.currentTimeMillis()

    override fun onCreate() {
        super.onCreate()
        Log.d("App Limit Service", "Service created")

        appUsageTracker = AppUsageTracker(this)
        notificationManager = NotificationManager(this)
        overlayManager = OverlayManager(this)
        audioManager = AudioManager(this)

        startForeground(1, notificationManager.createNotificationChannel())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("App Limit Service", "Service started")
        trackAppUsage()
        return START_STICKY
    }

    private fun trackAppUsage() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                Log.d("Track App Usages", "Checking app usage")
                updateAppUsage()
                checkAppLimits()
                handler.postDelayed(this, 5000) // Check every 5 seconds
            }
        }, 5000)
    }

    private fun updateAppUsage() {
        val newApp = appUsageTracker.getForegroundAppPackage()
        val now = System.currentTimeMillis()

        // Update usage time for the previous app
        if (currentApp != null) {
            val usageTime = appUsageMap.getOrDefault(currentApp, 0L)
            appUsageMap[currentApp!!] = usageTime + (now - lastCheckedTime)
        }

        // Update current app and time
        currentApp = newApp
        lastCheckedTime = now
    }

    private fun checkAppLimits() {
        currentApp?.let { foregroundApp ->
            appLimits.forEach { appLimit ->
                val timeSpent = (appUsageMap[appLimit.packageName] ?: 0L) / 1000 // Convert to seconds
                if (timeSpent > appLimit.allowedTimeInSeconds && appLimit.packageName == foregroundApp) {
                    // Notify user and show overlay if the app exceeds the limit
                    notificationManager.notifyUser(appLimit.packageName)
                    overlayManager.showOverlay(appLimit.packageName)
                    audioManager.stopAudioPlayback()
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
        Log.d("App Limit Service", "Service destroyed")
    }
}
