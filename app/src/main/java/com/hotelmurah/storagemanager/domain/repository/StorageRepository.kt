package com.hotelmurah.storagemanager.domain.repository

import com.hotelmurah.storagemanager.domain.model.FileInfo
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    suspend fun scanStorage(): Flow<List<FileInfo>>
    suspend fun getStorageStats(): Flow<StorageStats>
    suspend fun findDuplicateFiles(): Flow<List<List<FileInfo>>>
    suspend fun deleteFiles(files: List<FileInfo>): Boolean
    suspend fun cleanCache(): Long
    suspend fun getFilesByType(type: FileType): Flow<List<FileInfo>>
    suspend fun calculateMd5(file: FileInfo): String?
    suspend fun addExcludedFolder(path: String)
    suspend fun removeExcludedFolder(path: String)
    suspend fun getExcludedFolders(): Flow<List<ExcludedFolder>>
    suspend fun getFilesSortedBySize(ascending: Boolean = false): Flow<List<FileInfo>>
}

data class StorageStats(
    val totalSpace: Long,
    val usedSpace: Long,
    val freeSpace: Long,
    val categoryStats: Map<FileType, Long>
)
