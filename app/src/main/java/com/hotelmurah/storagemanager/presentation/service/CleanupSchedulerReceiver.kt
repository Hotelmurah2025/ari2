package com.hotelmurah.storagemanager.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CleanupSchedulerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val repository = StorageRepositoryImpl(
                    context,
                    (context.applicationContext as StorageManagerApp).database.fileDao(),
                    (context.applicationContext as StorageManagerApp).database.excludedFolderDao()
                )
                
                // Clean cache files
                repository.cleanCache()
                
                // Clean old downloads (files older than 30 days)
                val oldFiles = repository.scanStorage()
                    .first()
                    .filter { file ->
                        val age = System.currentTimeMillis() - file.lastModified
                        val isInDownloads = file.path.contains("Download", ignoreCase = true)
                        age > 30 * 24 * 60 * 60 * 1000L && isInDownloads // 30 days
                    }
                
                if (oldFiles.isNotEmpty()) {
                    repository.deleteFiles(oldFiles)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
