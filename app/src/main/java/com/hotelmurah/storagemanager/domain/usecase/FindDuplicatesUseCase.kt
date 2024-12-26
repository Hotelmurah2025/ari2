package com.hotelmurah.storagemanager.domain.usecase

import com.hotelmurah.storagemanager.domain.model.FileInfo
import com.hotelmurah.storagemanager.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindDuplicatesUseCase @Inject constructor(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(): Flow<List<List<FileInfo>>> = repository.findDuplicateFiles()
}
