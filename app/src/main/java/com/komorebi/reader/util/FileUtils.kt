package com.komorebi.reader.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.komorebi.reader.KomorebiApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Utility functions for file handling, formatting, and common operations
 */
object FileUtils {

    private const val TAG = "FileUtils"

    /**
     * Get the file name from a URI
     */
    fun getFileName(context: Context, uri: Uri): String {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0) cursor.getString(nameIndex) else "unknown"
            } else "unknown"
        } ?: "unknown"
    }

    /**
     * Get file extension from URI or path
     */
    fun getFileExtension(uri: Uri): String {
        val path = uri.path ?: return ""
        val lastDot = path.lastIndexOf('.')
        return if (lastDot >= 0) path.substring(lastDot + 1).lowercase() else ""
    }

    /**
     * Get file extension from file name
     */
    fun getFileExtension(fileName: String): String {
        val lastDot = fileName.lastIndexOf('.')
        return if (lastDot >= 0) fileName.substring(lastDot + 1).lowercase() else ""
    }

    /**
     * Check if file is a supported book format
     */
    fun isSupportedBookFile(uri: Uri): Boolean {
        val ext = getFileExtension(uri)
        return ext in setOf("cbz", "cbr", "pdf", "epub", "zip", "rar")
    }

    fun isSupportedBookFile(fileName: String): Boolean {
        val ext = getFileExtension(fileName)
        return ext in setOf("cbz", "cbr", "pdf", "epub", "zip", "rar")
    }

    /**
     * Get the books directory for storing downloads
     */
    fun getBooksDirectory(context: Context): File {
        val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Komorebi/Books")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /**
     * Get the covers cache directory
     */
    fun getCoversCacheDir(context: Context): File {
        val dir = File(context.cacheDir, "covers")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /**
     * Get a unique file name for download
     */
    fun generateUniqueFileName(directory: File, baseName: String, extension: String): File {
        var file = File(directory, "$baseName.$extension")
        var counter = 1
        while (file.exists()) {
            file = File(directory, "${baseName}_$counter.$extension")
            counter++
        }
        return file
    }

    /**
     * Format file size for display
     */
    fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> String.format(Locale.getDefault(), "%.1f KB", bytes / 1024.0)
            bytes < 1024 * 1024 * 1024 -> String.format(Locale.getDefault(), "%.1f MB", bytes / (1024.0 * 1024.0))
            else -> String.format(Locale.getDefault(), "%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0))
        }
    }

    /**
     * Format duration for display
     */
    fun formatDuration(millis: Long): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "$days j"
            hours > 0 -> "$hours h"
            minutes > 0 -> "$minutes min"
            else -> "$seconds s"
        }
    }

    /**
     * Format relative time (e.g., "2 jours", "1 semaine")
     */
    fun formatRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        return formatDuration(diff)
    }

    /**
     * Copy file from URI to local storage
     */
    suspend fun copyUriToFile(context: Context, uri: Uri, destination: File): Boolean = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destination).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to copy file", e)
            false
        }
    }

    /**
     * Get MIME type for file extension
     */
    fun getMimeType(extension: String): String {
        return when (extension.lowercase()) {
            "pdf" -> "application/pdf"
            "epub" -> "application/epub+zip"
            "cbz", "zip" -> "application/zip"
            "cbr", "rar" -> "application/x-rar-compressed"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            "gif" -> "image/gif"
            "bmp" -> "image/bmp"
            else -> "application/octet-stream"
        }
    }

    /**
     * Delete file recursively
     */
    fun deleteRecursively(file: File): Boolean {
        return if (file.isDirectory) {
            file.listFiles()?.all { deleteRecursively(it) } == true && file.delete()
        } else {
            file.delete()
        }
    }

    /**
     * Get directory size
     */
    fun getDirectorySize(file: File): Long {
        if (!file.exists()) return 0
        if (file.isFile) return file.length()
        return file.listFiles()?.sumOf { getDirectorySize(it) } ?: 0
    }

    /**
     * Create a shareable URI for a file
     */
    fun getShareableUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}

/**
 * String formatting utilities
 */
object StringUtils {

    /**
     * Truncate string with ellipsis
     */
    fun truncate(str: String, maxLength: Int): String {
        return if (str.length <= maxLength) str else "${str.take(maxLength - 1)}…"
    }

    /**
     * Capitalize first letter of each word
     */
    fun capitalizeWords(str: String): String {
        return str.split(" ").joinToString(" ") { it.replaceFirstChar { it.uppercase() } }
    }

    /**
     * Clean filename for use as title
     */
    fun cleanFileName(fileName: String): String {
        return fileName
            .substringBeforeLast(".")
            .replace(Regex("[_\-]+"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
            .let { capitalizeWords(it) }
    }

    /**
     * Generate search-friendly string
     */
    fun toSearchString(str: String): String {
        return str
            .lowercase()
            .replace(Regex("[^a-z0-9\\s]"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}

/**
 * Date/Time formatting utilities
 */
object DateUtils {

    private val DATE_FORMAT = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val TIME_FORMAT = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val DATETIME_FORMAT = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

    fun formatDate(timestamp: Long): String = DATE_FORMAT.format(timestamp)
    fun formatTime(timestamp: Long): String = TIME_FORMAT.format(timestamp)
    fun formatDateTime(timestamp: Long): String = DATETIME_FORMAT.format(timestamp)
}

/**
 * Number formatting utilities
 */
object NumberUtils {

    /**
     * Format number with compact notation (1.2K, 3.4M, etc.)
     */
    fun formatCompact(number: Long): String {
        return when {
            number < 1000 -> number.toString()
            number < 1_000_000 -> String.format(Locale.getDefault(), "%.1fK", number / 1000.0)
            number < 1_000_000_000 -> String.format(Locale.getDefault(), "%.1fM", number / 1_000_000.0)
            else -> String.format(Locale.getDefault(), "%.1fB", number / 1_000_000_000.0)
        }
    }

    /**
     * Format percentage
     */
    fun formatPercent(value: Float, decimals: Int = 0): String {
        val format = "%.${decimals}f%%"
        return String.format(Locale.getDefault(), format, value * 100)
    }

    /**
     * Format rating (e.g., 4.8)
     */
    fun formatRating(rating: Float): String {
        return String.format(Locale.getDefault(), "%.1f", rating)
    }
}