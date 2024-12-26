package com.hotelmurah.storagemanager.domain.usecase

import com.hotelmurah.storagemanager.domain.model.FileInfo
import com.hotelmurah.storagemanager.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for detecting and managing duplicate files on the device.
 *
 * Implements an efficient three-step duplicate detection algorithm:
 * 1. Initial Grouping (O(n))
 *    - Groups files by size
 *    - Quick filtering of unique files
 *    - Memory-efficient processing
 *
 * 2. Name Pattern Analysis
 *    - Analyzes file name patterns
 *    - Identifies common duplicate patterns
 *    - Reduces MD5 calculation needs
 *
 * 3. MD5 Verification
 *    - Calculates MD5 only for potential duplicates
 *    - Chunk-based processing for large files
 *    - Parallel processing using coroutines
 *
 * Performance Optimizations:
 * - Only calculates MD5 for files with matching sizes
 * - Uses coroutines for parallel processing
 * - Implements memory-efficient chunk processing
 * - Caches results in Room database
 *
 * @property repository Storage repository for file operations
 */
class FindDuplicatesUseCase @Inject constructor(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(): Flow<List<List<FileInfo>>> = repository.findDuplicateFiles()
}
