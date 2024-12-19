package com.example.limit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.limit.presentation.screen.MainScreen
import com.example.limit.ui.theme.LimitTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LimitTheme {
                MainScreen()
            }
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


