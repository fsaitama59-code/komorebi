package com.komorebi.reader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ForeignKey
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Entity(
    tableName = "reading_progress",
    indices = [
        Index(value = ["bookId", "chapterId"], unique = true),
        Index("updatedAt")
    ],
    foreignKeys = [
        ForeignKey(entity = BookEntity::class, parentColumns = ["id"], childColumns = ["bookId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ChapterEntity::class, parentColumns = ["id"], childColumns = ["chapterId"], onDelete = ForeignKey.CASCADE)
    ]
)
@Serializable
data class ReadingProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: Long,
    val chapterId: Long,
    val pageIndex: Int = 0,
    val totalPages: Int = 0,
    val progress: Float = 0f,       // 0-1 within chapter
    val overallProgress: Float = 0f, // 0-1 across book
    val readingDirection: String = "rtl",
    val zoomLevel: Float = 1f,
    val theme: String = "dark",
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val deviceId: String = ""       // For sync conflict resolution
)