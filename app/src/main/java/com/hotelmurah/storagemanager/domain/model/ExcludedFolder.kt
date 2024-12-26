package com.hotelmurah.storagemanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "excluded_folders")
data class ExcludedFolder(
    @PrimaryKey val path: String,
    val dateAdded: Long = System.currentTimeMillis()
)
