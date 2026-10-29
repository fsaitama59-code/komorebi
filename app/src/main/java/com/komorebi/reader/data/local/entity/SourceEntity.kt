package com.komorebi.reader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Entity(
    tableName = "sources",
    indices = [Index(value = ["id"], unique = true)]
)
@Serializable
data class SourceEntity(
    @PrimaryKey val id: String,           // e.g., "mangadex", "mangaplus"
    val name: String,
    val baseUrl: String,
    val apiUrl: String?,
    val iconUrl: String?,
    val enabled: Boolean = true,
    val priority: Int = 0,
    val requiresAuth: Boolean = false,
    val authToken: String? = null,
    val settingsJson: String = "{}",      // Source-specific settings
    val lastSyncAt: LocalDateTime? = null,
    val lastError: String? = null,
    val version: String = "1.0.0"
)