package com.hotelmurah.storagemanager

import android.app.Application
import androidx.room.Room
import com.hotelmurah.storagemanager.data.local.StorageDatabase

class StorageManagerApp : Application() {
    lateinit var database: StorageDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            StorageDatabase::class.java,
            "storage_db"
        ).build()
    }
}
