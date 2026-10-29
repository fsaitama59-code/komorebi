package com.komorebi.reader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ForeignKey
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Entity(
    tableName = "chapters",
    indices = [
        Index(value = ["bookId", "sourceChapterId"], unique = true),
        Index("number"),
        Index("publishedAt")
    ],
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Serializable
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: Long,
    val sourceChapterId: String,    // Unique within source
    val title: String,
    val number: Double,
    val volume: Double? = null,
    val pageCount: Int = 0,
    val pagesJson: String = "[]",   // JSON array of page URLs/paths
    val publishedAt: LocalDateTime? = null,
    val downloadedAt: LocalDateTime? = null,
    val localPath: String? = null,  // Path to downloaded CBZ/folder
    val isRead: Boolean = false,
    val readAt: LocalDateTime? = null,
    val sortOrder: Int = 0
)