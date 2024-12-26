package com.hotelmurah.storagemanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files")
data class FileEntity(
    @PrimaryKey val path: String,
    val name: String,
    val size: Long,
    val type: String,
    val lastModified: Long,
    val md5Hash: String? = null
)
