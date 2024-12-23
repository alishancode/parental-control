package com.example.limit

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.limit.presentation.screen.MainScreen
import com.example.limit.services.AppLimitService
import com.example.limit.ui.theme.LimitTheme

class MainActivity : ComponentActivity() {

    // Register for Activity Result API to handle notification permission
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d("Notification Permission", "Granted")
                checkDrawOverOtherAppsPermission()
            } else {
                Log.d("Notification Permission", "Denied")
            }
        }

    // Register for Activity Result API to handle Draw Over Other Apps permission
    private val requestDrawOverOtherAppsPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (Settings.canDrawOverlays(this)) {
                Log.d("Draw Over Permission", "Granted")
                startAppLimitService()
            } else {
                Log.d("Draw Over Permission", "Denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check and request notification permissions (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                checkDrawOverOtherAppsPermission()
            }
        } else {
            checkDrawOverOtherAppsPermission()
        }

        setContent {
            LimitTheme {
                MainScreen()
            }
        }
    }

    private fun checkDrawOverOtherAppsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                requestDrawOverOtherAppsPermission.launch(intent)
            } else {
                startAppLimitService()
            }
        } else {
            startAppLimitService()
        }
    }

    private fun startAppLimitService() {
        val serviceIntent = Intent(this, AppLimitService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }
}

@Composable
fun ChatScreen() {
    Text(
        text = "Welcome to Chat!",
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun SettingsScreen() {
    Text(
        text = "Welcome to Settings!",
        modifier = Modifier.fillMaxSize()
    )
}
