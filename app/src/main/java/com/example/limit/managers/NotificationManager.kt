package com.example.limit.managers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.limit.R

class NotificationManager(private val context: Context) {

    private val CHANNEL_ID = "AppLimitServiceChannel"

    fun createNotificationChannel(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "App Limit Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("App Limit Monitoring")
            .setContentText("Monitoring app usage...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    fun notifyUser(packageName: String) {
        val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("App Limit Reached")
            .setContentText("You have exceeded the usage limit for $packageName.")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }
}
