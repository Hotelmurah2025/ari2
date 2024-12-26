package com.hotelmurah.storagemanager.domain.usecase

import com.hotelmurah.storagemanager.domain.model.FileInfo
import com.hotelmurah.storagemanager.domain.repository.StorageRepository
import javax.inject.Inject

class DeleteFilesUseCase @Inject constructor(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(files: List<FileInfo>): Boolean = repository.deleteFiles(files)
}
