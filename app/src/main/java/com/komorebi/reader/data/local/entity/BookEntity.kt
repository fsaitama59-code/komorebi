package com.komorebi.reader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Entity(
    tableName = "books",
    indices = [
        Index(value = ["sourceId", "sourceKey"], unique = true),
        Index("lastReadAt"),
        Index("addedAt"),
        Index("title"),
        Index("author")
    ]
)
@Serializable
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sourceId: String,           // e.g., "mangadex", "local", "komga"
    val sourceKey: String,          // Unique key within source
    val title: String,
    val author: String,
    val description: String? = null,
    val coverUrl: String? = null,
    val localCoverPath: String? = null,
    val type: BookType = BookType.Manga,
    val status: BookStatus = BookStatus.Ongoing,
    val tags: String = "[]",        // JSON array
    val rating: Float = 0f,
    val chapterCount: Int = 0,
    val lastChapterRead: Long = 0,  // ChapterEntity id
    val lastPageRead: Int = 0,
    val readingProgress: Float = 0f, // 0-1
    val isFavorite: Boolean = false,
    val isDownloaded: Boolean = false,
    val downloadPath: String? = null,
    val addedAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val lastReadAt: LocalDateTime? = null,
    val sourceMetadata: String = "{}" // JSON for source-specific data
)

enum class BookType {
    Manga, Novel, Comic, Magazine, Webtoon, Other
}

enum class BookStatus {
    Ongoing, Completed, Hiatus, Cancelled, Unknown
}