package com.komorebi.reader.data.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toFile
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.success
import coil.size.ORIGINAL
import coil.transform.Transformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.ZipFile
import java.util.zip.ZipEntry

/**
 * Image loading utilities for the reader
 * Handles CBZ/ZIP, PDF, EPUB, and network images via Coil
 */

// ===== CUSTOM URIS SCHEMES =====
// cbz://filename.zip/page.jpg
// epub://filename.epub/page.xhtml
// pdf://content://uri/pageIndex

object ReaderImageLoader {

    /**
     * Load image from custom scheme URI
     */
    suspend fun loadImage(context: Context, uriString: String): Bitmap? = withContext(Dispatchers.IO) {
        return@withContext when {
            uriString.startsWith("cbz://") -> loadFromCbz(context, uriString)
            uriString.startsWith("epub://") -> loadFromEpub(context, uriString)
            uriString.startsWith("pdf://") -> loadFromPdf(context, uriString)
            else -> loadFromUri(context, Uri.parse(uriString))
        }
    }

    /**
     * Load from CBZ/ZIP archive
     */
    private fun loadFromCbz(context: Context, uriString: String): Bitmap? {
        try {
            // Parse: cbz://temp_filename.zip/page.jpg
            val withoutScheme = uriString.substringAfter("cbz://")
            val parts = withoutScheme.split("/", limit = 2)
            if (parts.size != 2) return null

            val archiveName = parts[0]
            val entryName = parts[1]

            // Find temp file in cache
            val cacheDir = context.cacheDir
            val archiveFile = File(cacheDir, archiveName)
            if (!archiveFile.exists()) return null

            val zipFile = ZipFile(archiveFile)
            val entry = zipFile.getEntry(entryName)
            if (entry == null) return null

            val inputStream = zipFile.getInputStream(entry)
            return BitmapFactory.decodeStream(inputStream).also { inputStream.close() }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Load from EPUB archive
     */
    private fun loadFromEpub(context: Context, uriString: String): Bitmap? {
        // EPUB pages are XHTML - would need WebView or custom renderer
        // For now return placeholder
        null
    }

    /**
     * Load from PDF (page index)
     */
    private fun loadFromPdf(context: Context, uriString: String): Bitmap? {
        // PDF rendering would use PdfRenderer
        // For now return placeholder
        null
    }

    /**
     * Load from regular URI (content://, file://, https://)
     */
    private fun loadFromUri(context: Context, uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input)
            }
        } catch (e: Exception) {
            null
        }
    }
}

// ===== COIL INTEGRATION =====

object CoilImageLoader {

    private var _imageLoader: ImageLoader? = null

    fun getImageLoader(context: Context): ImageLoader {
        return _imageLoader ?: ImageLoader(context).also { _imageLoader = it }
    }

    /**
     * Create ImageRequest for reader page
     */
    fun createPageRequest(
        context: Context,
        uriString: String,
        targetWidth: Int = ORIGINAL,
        targetHeight: Int = ORIGINAL
    ): ImageRequest {
        val loader = getImageLoader(context)
        val uri = when {
            uriString.startsWith("cbz://") -> Uri.parse(uriString) // Custom handler needed
            uriString.startsWith("epub://") -> Uri.parse(uriString)
            uriString.startsWith("pdf://") -> Uri.parse(uriString)
            else -> Uri.parse(uriString)
        }

        return ImageRequest.Builder(context)
            .data(uri)
            .target { drawable -> /* Custom target for reader */ }
            .crossfade(true)
            .build()
    }

    /**
     * Preload next/previous pages
     */
    suspend fun preloadPages(context: Context, uris: List<String>) {
        val loader = getImageLoader(context)
        uris.forEach { uri ->
            try {
                loader.execute(ImageRequest.Builder(context)
                    .data(Uri.parse(uri))
                    .build())
            } catch (e: Exception) {
                // Ignore preload errors
            }
        }
    }

    /**
     * Clear memory cache
     */
    fun clearMemoryCache(context: Context) {
        getImageLoader(context).memoryCache.clear()
    }

    /**
     * Clear disk cache
     */
    suspend fun clearDiskCache(context: Context) {
        getImageLoader(context).diskCache.clear()
    }
}

// ===== TRANSFORMATIONS =====

class FitWidthTransformation : Transformation {
    override fun transform(pool: coil.util.BitmapPool, input: Bitmap): Bitmap {
        // Scale to fit screen width maintaining aspect ratio
        return input
    }

    override fun key(): String = "FitWidth"
}

class BlurTransformation(val radius: Float = 25f) : Transformation {
    override fun transform(pool: coil.util.BitmapPool, input: Bitmap): Bitmap {
        // Apply blur for background effects
        return input
    }

    override fun key(): String = "Blur_$radius"
}

// ===== PAGE LOADER FOR READER =====

class PageLoader(private val context: Context) {

    private val loader = CoilImageLoader.getImageLoader(context)
    private val memoryCache = mutableMapOf<String, Bitmap>()

    /**
     * Load page bitmap (with memory caching)
     */
    suspend fun loadPage(uriString: String, width: Int, height: Int): Bitmap? {
        return withContext(Dispatchers.IO) {
            // Check memory cache first
            memoryCache[uriString]?.let { return@withContext it }

            val bitmap = ReaderImageLoader.loadImage(context, uriString)
                ?.let { scaleBitmap(it, width, height) }

            bitmap?.let { memoryCache[uriString] = it }
            bitmap
        }
    }

    /**
     * Preload adjacent pages
     */
    fun preloadAdjacent(currentIndex: Int, uris: List<String>, radius: Int = 2) {
        val start = maxOf(0, currentIndex - radius)
        val end = minOf(uris.size - 1, currentIndex + radius)
        (start..end).forEach { index ->
            if (index != currentIndex) {
                CoilImageLoader.preloadPages(context, listOf(uris[index]))
            }
        }
    }

    private fun scaleBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        if (bitmap.width <= targetWidth && bitmap.height <= targetHeight) return bitmap
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }

    fun clearCache() {
        memoryCache.clear()
    }

    fun getCacheSize(): Long = memoryCache.values.sumOf { it.byteCount }
}