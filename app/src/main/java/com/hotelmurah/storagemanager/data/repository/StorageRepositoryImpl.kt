package com.hotelmurah.storagemanager.data.repository

import android.content.Context
import android.os.Environment
import android.os.StatFs
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hotelmurah.storagemanager.StorageManagerApp.Companion.CHANNEL_ID
import com.hotelmurah.storagemanager.data.local.FileDao
import com.hotelmurah.storagemanager.data.local.ExcludedFolderDao
import com.hotelmurah.storagemanager.domain.model.FileInfo
import com.hotelmurah.storagemanager.domain.model.FileType
import com.hotelmurah.storagemanager.domain.model.ExcludedFolder
import com.hotelmurah.storagemanager.domain.repository.StorageRepository
import com.hotelmurah.storagemanager.domain.repository.StorageStats
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

class StorageRepositoryImpl(
    private val context: Context,
    private val fileDao: FileDao,
    private val excludedFolderDao: ExcludedFolderDao
) : StorageRepository {
    private val excludedFolders = mutableSetOf<String>()
    
    init {
        CoroutineScope(Dispatchers.IO).launch {
            excludedFolderDao.getAllExcludedFolders().collect { folders ->
                excludedFolders.clear()
                excludedFolders.addAll(folders.map { it.path })
            }
        }
    }
    override suspend fun scanStorage(): Flow<List<FileInfo>> = flow {
        val files = mutableListOf<FileInfo>()
        withContext(Dispatchers.IO) {
            scanDirectory(Environment.getExternalStorageDirectory(), files)
        }
        emit(files)
    }

    private suspend fun scanDirectory(directory: File, files: MutableList<FileInfo>) {
        if (excludedFolders.any { directory.absolutePath.startsWith(it) }) {
            return
        }
        
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                scanDirectory(file, files)
            } else {
                files.add(
                    FileInfo(
                        path = file.absolutePath,
                        name = file.name,
                        size = file.length(),
                        type = getFileType(file),
                        lastModified = file.lastModified()
                    )
                )
                
                // Check if this is a large cache file
                if (file.length() > LARGE_CACHE_THRESHOLD && getFileType(file) == FileType.CACHE) {
                    notifyLargeCache(file)
                }
            }
        }
    }
    
    private fun notifyLargeCache(file: File) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Large Cache File Detected")
            .setContentText("${file.name} is using ${file.length() / 1024 / 1024}MB of storage")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), notification)
        }
    }
    
    companion object {
        private const val LARGE_CACHE_THRESHOLD = 100 * 1024 * 1024 // 100MB
        private const val CHANNEL_ID = "storage_manager_channel"
    }

    override suspend fun getStorageStats(): Flow<StorageStats> = flow {
        val stat = StatFs(Environment.getExternalStorageDirectory().path)
        val totalSpace = stat.totalBytes
        val freeSpace = stat.freeBytes
        val usedSpace = totalSpace - freeSpace

        val categoryStats = mutableMapOf<FileType, Long>()
        FileType.values().forEach { type ->
            categoryStats[type] = 0L
        }

        scanStorage().first().forEach { file ->
            categoryStats[file.type] = categoryStats[file.type]!! + file.size
        }

        emit(StorageStats(totalSpace, usedSpace, freeSpace, categoryStats))
    }

    override suspend fun findDuplicateFiles(): Flow<List<List<FileInfo>>> = flow {
        val fileGroups = scanStorage().first()
            .groupBy { Triple(it.size, getFileExtension(it.name), calculateMd5(it)) }
            .filter { it.value.size > 1 }
            .values
            .toList()
        emit(fileGroups)
    }

    override suspend fun deleteFiles(files: List<FileInfo>): Boolean = withContext(Dispatchers.IO) {
        var success = true
        files.forEach { fileInfo ->
            val file = File(fileInfo.path)
            if (!file.delete()) {
                success = false
            }
        }
        success
    }

    override suspend fun cleanCache(): Long = withContext(Dispatchers.IO) {
        var spaceSaved = 0L
        context.cacheDir.listFiles()?.forEach { file ->
            if (file.delete()) {
                spaceSaved += file.length()
            }
        }
        spaceSaved
    }

    override suspend fun getFilesByType(type: FileType): Flow<List<FileInfo>> =
        scanStorage().map { files ->
            files.filter { it.type == type }
        }

    override suspend fun addExcludedFolder(path: String) {
        excludedFolderDao.addExcludedFolder(ExcludedFolder(path))
    }

    override suspend fun removeExcludedFolder(path: String) {
        excludedFolderDao.removeExcludedFolder(ExcludedFolder(path))
    }

    override suspend fun getExcludedFolders(): Flow<List<ExcludedFolder>> =
        excludedFolderDao.getAllExcludedFolders()

    override suspend fun getFilesSortedBySize(ascending: Boolean): Flow<List<FileInfo>> =
        scanStorage().map { files ->
            if (ascending) {
                files.sortedBy { it.size }
            } else {
                files.sortedByDescending { it.size }
            }
        }

    override suspend fun calculateMd5(file: FileInfo): String? = withContext(Dispatchers.IO) {
        try {
            val md = MessageDigest.getInstance("MD5")
            File(file.path).inputStream().use { fis ->
                val buffer = ByteArray(8192)
                var read: Int
                while (fis.read(buffer).also { read = it } > 0) {
                    md.update(buffer, 0, read)
                }
            }
            BigInteger(1, md.digest()).toString(16).padStart(32, '0')
        } catch (e: Exception) {
            null
        }
    }

    private fun getFileType(file: File): FileType = when {
        isImageFile(file.name) -> FileType.IMAGE
        isVideoFile(file.name) -> FileType.VIDEO
        isDocumentFile(file.name) -> FileType.DOCUMENT
        isAudioFile(file.name) -> FileType.AUDIO
        isApkFile(file.name) -> FileType.APK
        isArchiveFile(file.name) -> FileType.ARCHIVE
        file.path.contains("cache", ignoreCase = true) -> FileType.CACHE
        else -> FileType.OTHER
    }

    private fun getFileExtension(fileName: String): String =
        fileName.substringAfterLast('.', "")

    private fun isImageFile(fileName: String) =
        fileName.matches(Regex(".*\\.(jpg|jpeg|png|gif|bmp|webp)$", RegexOption.IGNORE_CASE))

    private fun isVideoFile(fileName: String) =
        fileName.matches(Regex(".*\\.(mp4|avi|mkv|mov|wmv)$", RegexOption.IGNORE_CASE))

    private fun isDocumentFile(fileName: String) =
        fileName.matches(Regex(".*\\.(pdf|doc|docx|txt|xls|xlsx|ppt|pptx)$", RegexOption.IGNORE_CASE))

    private fun isAudioFile(fileName: String) =
        fileName.matches(Regex(".*\\.(mp3|wav|ogg|m4a)$", RegexOption.IGNORE_CASE))

    private fun isApkFile(fileName: String) =
        fileName.matches(Regex(".*\\.apk$", RegexOption.IGNORE_CASE))

    private fun isArchiveFile(fileName: String) =
        fileName.matches(Regex(".*\\.(zip|rar|7z|tar|gz)$", RegexOption.IGNORE_CASE))
}
