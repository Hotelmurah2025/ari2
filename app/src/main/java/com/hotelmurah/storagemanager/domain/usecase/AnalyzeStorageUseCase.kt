package com.hotelmurah.storagemanager.domain.usecase

import com.hotelmurah.storagemanager.domain.model.FileInfo
import com.hotelmurah.storagemanager.domain.repository.StorageRepository
import com.hotelmurah.storagemanager.domain.repository.StorageStats
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnalyzeStorageUseCase @Inject constructor(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(): Flow<StorageStats> = repository.getStorageStats()
    
    suspend fun getFilesByType(type: FileType): Flow<List<FileInfo>> = 
        repository.getFilesByType(type)
}
