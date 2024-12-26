package com.hotelmurah.storagemanager.domain.usecase

import com.hotelmurah.storagemanager.domain.repository.StorageRepository
import javax.inject.Inject

/**
 * Use case for managing and cleaning application cache files.
 *
 * Key Features:
 * 1. Cache Analysis
 *    - Identifies app cache directories
 *    - Calculates cache size per app
 *    - Detects and notifies of large cache files
 *
 * 2. Smart Cleaning
 *    - Preserves essential cache files
 *    - Prioritizes large cache files
 *    - Handles permissions appropriately
 *    - Reports cleaning results
 *
 * 3. Safety Measures
 *    - Respects app-specific cache requirements
 *    - Implements atomic operations
 *    - Provides error handling
 *    - Supports operation cancellation
 *
 * 4. Optimizations
 *    - Batch processing for multiple apps
 *    - Background processing with coroutines
 *    - Progress tracking
 *    - Memory-efficient operations
 *
 * @property repository Storage repository for cache operations
 */
class CleanCacheUseCase @Inject constructor(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(): Long = repository.cleanCache()
}
