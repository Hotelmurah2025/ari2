package com.hotelmurah.storagemanager.data.local

import androidx.room.*
import com.hotelmurah.storagemanager.domain.model.ExcludedFolder
import kotlinx.coroutines.flow.Flow

@Dao
interface ExcludedFolderDao {
    @Query("SELECT * FROM excluded_folders")
    fun getAllExcludedFolders(): Flow<List<ExcludedFolder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExcludedFolder(folder: ExcludedFolder)

    @Delete
    suspend fun removeExcludedFolder(folder: ExcludedFolder)
}
