package com.hotelmurah.storagemanager.domain.usecase

import com.hotelmurah.storagemanager.domain.model.FileInfo
import com.hotelmurah.storagemanager.domain.repository.StorageRepository
import com.hotelmurah.storagemanager.domain.repository.StorageStats
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for analyzing device storage and providing detailed storage statistics.
 *
 * Key Features:
 * 1. Total Storage Analysis
 *    - Calculates total, used, and free space
 *    - Handles both internal and external storage
 *    - Provides real-time updates during scanning
 *
 * 2. File Categorization
 *    - Groups files by type (images, videos, documents, etc.)
 *    - Calculates storage usage per category
 *    - Identifies large files and potential cleanup targets
 *
 * 3. Performance Optimizations
 *    - Uses MediaStore API for efficient media scanning
 *    - Implements batch processing for large directories
 *    - Respects excluded folders configuration
 *    - Memory-efficient handling of large file lists
 *
 * @property repository Storage repository that handles file system operations
 */
class AnalyzeStorageUseCase @Inject constructor(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(): Flow<StorageStats> = repository.getStorageStats()
    
    suspend fun getFilesByType(type: FileType): Flow<List<FileInfo>> = 
        repository.getFilesByType(type)
}
