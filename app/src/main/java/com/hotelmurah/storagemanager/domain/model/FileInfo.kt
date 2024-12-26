package com.hotelmurah.storagemanager.domain.model

data class FileInfo(
    val path: String,
    val name: String,
    val size: Long,
    val type: FileType,
    val lastModified: Long,
    val md5Hash: String? = null
)

enum class FileType {
    IMAGE,
    VIDEO,
    DOCUMENT,
    AUDIO,
    APK,
    ARCHIVE,
    CACHE,
    OTHER
}
