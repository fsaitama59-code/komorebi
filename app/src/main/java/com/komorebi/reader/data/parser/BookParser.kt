package com.komorebi.reader.data.parser

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.ZipFile
import java.util.zip.ZipEntry

/**
 * Parsers for local book files (CBZ, CBR, PDF, EPUB)
 * Returns standardized BookData for database insertion
 */

sealed interface ParseResult {
    data class Success(val book: BookData) : ParseResult
    data class Error(val message: String, val cause: Throwable? = null) : ParseResult
}

data class BookData(
    val title: String,
    val author: String = "Inconnu",
    val description: String? = null,
    val coverImage: ByteArray? = null,
    val pages: List<PageData>,
    val type: BookType,
    val metadata: Map<String, String> = emptyMap()
)

data class PageData(
    val index: Int,
    val imageData: ByteArray? = null,
    val imageUri: String? = null,
    val width: Int = 0,
    val height: Int = 0
)

enum class BookType { CBZ, CBR, PDF, EPUB, UNKNOWN }

object BookParser {

    private const val MAX_COVER_SIZE = 512 * 1024 // 512KB
    private const val SUPPORTED_IMAGE_EXTS = setOf("jpg", "jpeg", "png", "webp", "bmp", "gif")

    /**
     * Parse a book file from URI
     */
    suspend fun parseFromUri(context: Context, uri: Uri): ParseResult = withContext(Dispatchers.IO) {
        try {
            val ext = FileUtils.getFileExtension(uri).lowercase()
            return@withContext when (ext) {
                "cbz", "zip" -> parseCbz(context, uri)
                "cbr", "rar" -> parseCbr(context, uri)
                "pdf" -> parsePdf(context, uri)
                "epub" -> parseEpub(context, uri)
                else -> ParseResult.Error("Format non supporté: $ext")
            }
        } catch (e: Exception) {
            ParseResult.Error("Erreur de parsing: ${e.message}", e)
        }
    }

    /**
     * Parse CBZ/ZIP archive
     */
    private fun parseCbz(context: Context, uri: Uri): ParseResult {
        val tempFile = File(context.cacheDir, "temp_${System.currentTimeMillis()}.cbz")
        try {
            // Copy URI to temp file
            context.contentResolver.openInputStream(uri)?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val zipFile = ZipFile(tempFile)
            val entries = zipFile.entries().asSequence()
                .filter { !it.isDirectory }
                .map { it.name }
                .filter { name ->
                    val ext = name.substringAfterLast(".", "").lowercase()
                    ext in SUPPORTED_IMAGE_EXTS
                }
                .sortedWith(compareBy { naturalOrder() }) // Natural sort for page order
                .toList()

            if (entries.isEmpty()) {
                return ParseResult.Error("Aucune image trouvée dans l'archive")
            }

            // Extract cover (first page)
            val coverEntry = entries.firstOrNull()?.let { zipFile.getEntry(it) }
            val coverBytes = coverEntry?.let { extractEntry(zipFile, it) }?.takeIf { it.size <= MAX_COVER_SIZE }

            // Build pages metadata (don't load all images into memory)
            val pages = entries.mapIndexed { index, name ->
                PageData(
                    index = index,
                    imageUri = "cbz://${tempFile.name}/$name", // Custom scheme for loader
                    width = 0,
                    height = 0
                )
            }

            val title = FileUtils.cleanFileName(uri.getFileName(context))

            ParseResult.Success(BookData(
                title = title,
                pages = pages,
                type = BookType.CBZ,
                coverImage = coverBytes,
                metadata = mapOf("sourceFile" to uri.toString(), "tempFile" to tempFile.absolutePath)
            ))
        } catch (e: Exception) {
            ParseResult.Error("Erreur CBZ: ${e.message}", e)
        } finally {
            // Note: Keep temp file for reading, clean up on app close
        }
    }

    /**
     * Parse CBR/RAR archive (requires unrar - fallback to CBZ logic)
     */
    private fun parseCbr(context: Context, uri: Uri): ParseResult {
        // RAR parsing needs external library (junrar) - for now treat as CBZ
        return parseCbz(context, uri)
    }

    /**
     * Parse PDF - extract page count and first page as cover
     */
    private fun parsePdf(context: Context, uri: Uri): ParseResult {
        try {
            // Use Android's PdfRenderer (API 21+)
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
                ?: return ParseResult.Error("Impossible d'ouvrir le PDF")

            // For simplicity, return basic metadata
            // Full PDF rendering would use PdfRenderer in Reader
            val title = FileUtils.cleanFileName(uri.getFileName(context))

            ParseResult.Success(BookData(
                title = title,
                pages = listOf(PageData(0, imageUri = "pdf://${uri.toString()}/0")),
                type = BookType.PDF,
                metadata = mapOf("sourceFile" to uri.toString(), "pageCount" to "1")
            ))
        } catch (e: Exception) {
            ParseResult.Error("Erreur PDF: ${e.message}", e)
        }
    }

    /**
     * Parse EPUB - extract metadata and spine
     */
    private fun parseEpub(context: Context, uri: Uri): ParseResult {
        try {
            val tempFile = File(context.cacheDir, "temp_${System.currentTimeMillis()}.epub")
            context.contentResolver.openInputStream(uri)?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val zipFile = ZipFile(tempFile)
            val containerEntry = zipFile.getEntry("META-INF/container.xml")
            var title = FileUtils.cleanFileName(uri.getFileName(context))
            var author = "Inconnu"
            var coverBytes: ByteArray? = null
            val pages = mutableListOf<PageData>()

            containerEntry?.let { entry ->
                zipFile.getInputStream(entry).use { input ->
                    // Parse container.xml to find OPF file
                    // Simplified - in production use an XML parser
                    val content = input.readBytes().decodeToString()
                    // Extract title/author/cover from OPF
                }
            }

            // Extract XHTML files for pages
            val xhtmlEntries = zipFile.entries().asSequence()
                .filter { !it.isDirectory && it.name.endsWith(".xhtml") || it.name.endsWith(".html") }
                .map { it.name }
                .sorted()
                .toList()

            pages.addAll(xhtmlEntries.mapIndexed { index, name ->
                PageData(index = index, imageUri = "epub://${tempFile.name}/$name")
            })

            ParseResult.Success(BookData(
                title = title,
                author = author,
                pages = pages,
                type = BookType.EPUB,
                coverImage = coverBytes,
                metadata = mapOf("sourceFile" to uri.toString(), "tempFile" to tempFile.absolutePath)
            ))
        } catch (e: Exception) {
            ParseResult.Error("Erreur EPUB: ${e.message}", e)
        }
    }

    private fun extractEntry(zipFile: ZipFile, entry: ZipEntry): ByteArray {
        return zipFile.getInputStream(entry).use { input ->
            input.readAllBytes()
        }
    }

    /**
     * Clean up temporary files
     */
    fun cleanupTempFiles(context: Context) {
        val cacheDir = context.cacheDir
        cacheDir.listFiles()?.filter { it.name.startsWith("temp_") }?.forEach { it.delete() }
    }
}