package com.hotelmurah.storagemanager.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // Will implement storage management logic here
    fun analyzeStorage() {
        viewModelScope.launch {
            // Will implement storage analysis
        }
    }
}
