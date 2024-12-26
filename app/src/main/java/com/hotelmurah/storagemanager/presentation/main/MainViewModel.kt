package com.hotelmurah.storagemanager.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotelmurah.storagemanager.domain.model.FileInfo
import com.hotelmurah.storagemanager.domain.model.FileType
import com.hotelmurah.storagemanager.domain.repository.StorageStats
import com.hotelmurah.storagemanager.domain.usecase.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val analyzeStorageUseCase: AnalyzeStorageUseCase,
    private val cleanCacheUseCase: CleanCacheUseCase,
    private val findDuplicatesUseCase: FindDuplicatesUseCase,
    private val deleteFilesUseCase: DeleteFilesUseCase,
    private val scheduleCleanupUseCase: ScheduleCleanupUseCase
) : ViewModel() {
    private val _storageStats = MutableStateFlow<StorageStats?>(null)
    val storageStats: StateFlow<StorageStats?> = _storageStats.asStateFlow()

    private val _duplicateFiles = MutableStateFlow<List<List<FileInfo>>>(emptyList())
    val duplicateFiles: StateFlow<List<List<FileInfo>>> = _duplicateFiles.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private var scanJob: Job? = null

    fun analyzeStorage() {
        scanJob?.cancel()
        scanJob = viewModelScope.launch {
            _isScanning.value = true
            try {
                analyzeStorageUseCase().collect { stats ->
                    _storageStats.value = stats
                }
            } finally {
                _isScanning.value = false
            }
        }
    }

    fun findDuplicates() {
        viewModelScope.launch {
            findDuplicatesUseCase().collect { duplicates ->
                _duplicateFiles.value = duplicates
            }
        }
    }

    fun cleanCache() {
        viewModelScope.launch {
            cleanCacheUseCase()
            analyzeStorage() // Refresh storage stats
        }
    }

    fun deleteFiles(files: List<FileInfo>) {
        viewModelScope.launch {
            if (deleteFilesUseCase(files)) {
                analyzeStorage() // Refresh storage stats
            }
        }
    }

    fun scheduleAutomaticCleanup(intervalHours: Int) {
        scheduleCleanupUseCase.schedule(intervalHours)
    }

    fun cancelAutomaticCleanup() {
        scheduleCleanupUseCase.cancel()
    }

    fun stopScanning() {
        scanJob?.cancel()
        _isScanning.value = false
    }

    override fun onCleared() {
        super.onCleared()
        scanJob?.cancel()
    }
}
