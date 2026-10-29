package com.komorebi.reader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Entity(
    tableName = "library_scans",
    indices = [Index("startedAt")]
)
@Serializable
data class LibraryScanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rootPath: String,
    val status: ScanStatus = ScanStatus.Pending,
    val totalFiles: Int = 0,
    val processedFiles: Int = 0,
    val newBooks: Int = 0,
    val updatedBooks: Int = 0,
    val errors: Int = 0,
    val startedAt: LocalDateTime = LocalDateTime.now(),
    val completedAt: LocalDateTime? = null,
    val errorLog: String = "[]"
)

enum class ScanStatus {
    Pending, Running, Completed, Failed, Cancelled
}