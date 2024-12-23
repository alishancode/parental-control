package com.example.limit.services
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.example.limit.R

data class AppLimit(
    val packageName: String,
    val allowedTimeInSeconds: Int
)

class AppLimitService : Service() {
    private val CHANNEL_ID = "AppLimitServiceChannel"
    private val handler = Handler(Looper.getMainLooper())
    private var overlayView: View? = null
    private lateinit var windowManager: WindowManager
    private lateinit var audioManager: AudioManager


    // List of apps and their time limits
    private val appLimits = listOf(
        AppLimit("com.google.android.youtube", 15) // Limit YouTube usage to 15 seconds
    )

    private var foregroundAppPackage: String? = null // Store the currently foreground app package

    override fun onCreate() {
        super.onCreate()
        Log.d("App Limit Service", "Service created")
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        startForeground(1, createNotificationChannel())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("App Limit Service", "Service started")
        trackAppUsage()
        return START_STICKY
    }

    private fun trackAppUsage() {
        Log.d("Track App Usages..", "Service started")
        handler.postDelayed(object : Runnable {
            override fun run() {
                Log.d("Running every","Checking app usage")
                checkAppLimits()
                handler.postDelayed(this, 5000) // Check every 5 seconds
            }
        }, 5000)
    }

    private fun checkAppLimits() {
        val usageStatsManager =
            getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 24 * 60 * 60 * 1000 // Last 24 hours

        // Get usage stats for the last 24 hours
        val usageStats: List<UsageStats> = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        )

        // Find the currently foreground app
        val recentAppUsage = usageStats.maxByOrNull { it.lastTimeUsed }
        foregroundAppPackage = recentAppUsage?.packageName

        // Check if the foreground app exceeds its limit
        usageStats.forEach { stat ->
            val packageName = stat.packageName
            val timeSpent = stat.totalTimeInForeground / 1000 // Convert to seconds
            val limit = appLimits.find { it.packageName == packageName }?.allowedTimeInSeconds

            if (limit != null && timeSpent > limit && packageName == foregroundAppPackage) {
                // Only notify and show overlay if the foreground app exceeds the limit
                notifyUser(packageName)
                showOverlay()
                stopAudioPlayback()

            }
        }
    }

    private fun stopAudioPlayback() {
        Log.d("YouTubeUsageService", "Stopping audio playback")
        if (audioManager.isMusicActive) {
            val keyEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE)
            audioManager.dispatchMediaKeyEvent(keyEvent)
        }
    }

    private fun notifyUser(packageName: String) {
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("App Limit Reached")
            .setContentText("You have exceeded the usage limit for $packageName.")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }

    private fun createNotificationChannel(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "App Limit Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("App Limit Monitoring")
            .setContentText("Monitoring app usage...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
        Log.d("App Limit Service", "Service destroyed")
    }

    private fun showOverlay() {
        if (overlayView != null) return // Avoid duplicates

        overlayView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#222831")) // Dark background
            setPadding(60, 60, 60, 60)
        }

        // Title
        val title = TextView(this).apply {
            text = "Time's Up for YouTube!"
            textSize = 24f
            setTextColor(Color.parseColor("#F05454")) // Red color
            gravity = Gravity.CENTER
        }

        // Description
        val description = TextView(this).apply {
            text = "Take a break and refocus. You've reached your 15-second limit!"
            textSize = 18f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 50)
        }

        // Go Home Button
        val goHomeButton = Button(this).apply {
            text = "Return to Home Screen"
            setBackgroundColor(Color.parseColor("#30475E")) // Blueish button
            setTextColor(Color.WHITE)
            setOnClickListener {
                // Remove overlay and redirect to the system home screen immediately
                removeOverlay()
                redirectToSystemHomeScreen()
            }
        }

        // Add views to layout
        (overlayView as LinearLayout).apply {
            addView(title)
            addView(description)
            addView(goHomeButton)
        }

        // Layout parameters
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        windowManager.addView(overlayView, layoutParams)
    }

    private fun removeOverlay() {
        overlayView?.let {
            windowManager.removeView(it)
            overlayView = null
        }
    }

    private fun redirectToSystemHomeScreen() {
        // Intent to go to the system's home screen (device home screen)
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
    }
}
