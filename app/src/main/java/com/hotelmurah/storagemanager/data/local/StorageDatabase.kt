package com.hotelmurah.storagemanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hotelmurah.storagemanager.data.local.entity.FileEntity

@Database(entities = [FileEntity::class], version = 1)
abstract class StorageDatabase : RoomDatabase() {
    abstract fun fileDao(): FileDao
}
