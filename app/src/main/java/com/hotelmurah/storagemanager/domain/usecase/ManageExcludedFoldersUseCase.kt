package com.hotelmurah.storagemanager.domain.usecase

import com.hotelmurah.storagemanager.data.local.ExcludedFolderDao
import com.hotelmurah.storagemanager.domain.model.ExcludedFolder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageExcludedFoldersUseCase @Inject constructor(
    private val excludedFolderDao: ExcludedFolderDao
) {
    fun getExcludedFolders(): Flow<List<ExcludedFolder>> =
        excludedFolderDao.getAllExcludedFolders()

    suspend fun addExcludedFolder(path: String) {
        excludedFolderDao.addExcludedFolder(ExcludedFolder(path))
    }

    suspend fun removeExcludedFolder(path: String) {
        excludedFolderDao.removeExcludedFolder(ExcludedFolder(path))
    }
}
