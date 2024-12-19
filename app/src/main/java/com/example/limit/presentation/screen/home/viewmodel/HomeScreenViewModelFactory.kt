package com.example.limit.presentation.screen.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.limit.data.repository.UsageStatsRepository

class HomeScreenViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            // Create x`x` instance
            val usageStatsRepository = UsageStatsRepository(context)
            // Return HomeScreenViewModel with UsageStatsRepository
            @Suppress("UNCHECKED_CAST")
            return HomeScreenViewModel(usageStatsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
