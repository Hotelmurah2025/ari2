package com.hotelmurah.storagemanager.domain.usecase

import com.hotelmurah.storagemanager.domain.repository.StorageRepository
import javax.inject.Inject

class CleanCacheUseCase @Inject constructor(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(): Long = repository.cleanCache()
}
