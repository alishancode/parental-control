package com.example.limit.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.limit.R

class OverlayManager(private val activity: Activity) {

    private var overlayView: LinearLayout? = null

    // Function to show the overlay
    fun showOverlay() {
        // Avoid duplicates
        if (overlayView != null) return

        // Create a LinearLayout to serve as the overlay container
        overlayView = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#222831")) // Dark background
            setPadding(60, 60, 60, 60)
        }

        // Title
        val title = TextView(activity).apply {
            text = "Time's Up for YouTube!"
            textSize = 24f
            setTextColor(Color.parseColor("#F05454")) // Red color
            gravity = Gravity.CENTER
        }

        // Description
        val description = TextView(activity).apply {
            text = "Take a break and refocus. You've reached your 15-second limit!"
            textSize = 18f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 50)
        }

        // Go Home Button
        val goHomeButton = Button(activity).apply {
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
        overlayView?.apply {
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

        // Add the overlay view to the window manager
        val windowManager = activity.getSystemService(Activity.WINDOW_SERVICE) as WindowManager
        windowManager.addView(overlayView, layoutParams)
    }

    // Function to remove the overlay
    private fun removeOverlay() {
        overlayView?.let {
            val windowManager = activity.getSystemService(Activity.WINDOW_SERVICE) as WindowManager
            windowManager.removeView(it)
            overlayView = null
        }
    }

    // Redirect to system home screen
    private fun redirectToSystemHomeScreen() {
        val intent = activity.packageManager.getLaunchIntentForPackage("com.android.launcher")?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        activity.startActivity(intent)
    }
}
