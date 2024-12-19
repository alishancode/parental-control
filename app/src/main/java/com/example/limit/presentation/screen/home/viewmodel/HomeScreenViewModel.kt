package com.example.limit.presentation.screen.home.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.limit.data.repository.UsageStatsRepository
import com.example.limit.data.model.UsageStatsModel
import com.example.limit.utils.isUsageStatsPermissionGranted

//class HomeScreenViewModel(private val usageStatsRepository: UsageStatsRepository) : ViewModel() {
//
//    // Corrected the mutableStateOf declaration
//    private val _appUsageStats = mutableStateOf<List<UsageStatsModel>>(emptyList())
//    val appUsageStats: State<List<UsageStatsModel>> get() = _appUsageStats
//
//    init {
//        loadAppUsageStats()
//    }
//
//    private fun loadAppUsageStats() {
//        _appUsageStats.value = usageStatsRepository.getAppUsageStats()
//    }
//}

// ViewModel
class HomeScreenViewModel(private val usageStatsRepository: UsageStatsRepository) : ViewModel() {

    private val _appUsageStats = mutableStateOf<List<UsageStatsModel>>(emptyList())
    val appUsageStats: State<List<UsageStatsModel>> get() = _appUsageStats

    private val _permissionGranted = mutableStateOf(false)
    val permissionGranted: State<Boolean> get() = _permissionGranted

    // Initialize with context being passed as a parameter
    fun checkAndLoadAppUsageStats(context: Context) {
        if (isUsageStatsPermissionGranted(context)) {
            _permissionGranted.value = true
            _appUsageStats.value = usageStatsRepository.getAppUsageStats()
        } else {
            _permissionGranted.value = false
        }
    }
}
