package com.example.limit.managers

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class OverlayManager(private val context: Context) {

    private var overlayView: View? = null
    private lateinit var windowManager: WindowManager

    init {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    fun showOverlay(packageName: String) {
        if (overlayView != null) return // Avoid duplicates

        overlayView = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#222831")) // Dark background
            setPadding(60, 60, 60, 60)
        }

        // Title
        val title = TextView(context).apply {
            text = "Time's Up for $packageName!"
            textSize = 24f
            setTextColor(Color.parseColor("#F05454")) // Red color
            gravity = Gravity.CENTER
        }

        // Description
        val description = TextView(context).apply {
            text = "Take a break and refocus. You've reached your limit!"
            textSize = 18f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 50)
        }

        // Go Home Button
        val goHomeButton = Button(context).apply {
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

    fun removeOverlay() {
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
        context.startActivity(homeIntent)
    }
}
