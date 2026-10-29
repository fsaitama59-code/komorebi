package com.komorebi.reader.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * File picker utilities for importing books
 */

@Composable
fun rememberFilePicker(
    onFilesSelected: (List<Uri>) -> Unit
): (Boolean) -> Unit {
    var multiple by remember { mutableStateOf(true) }

    val pickMultiple = remember {
        androidx.activity.compose.rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenMultipleDocuments()
        ) { uris: List<Uri> ->
            if (uris.isNotEmpty()) onFilesSelected(uris)
        }
    }

    val pickSingle = remember {
        androidx.activity.compose.rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let { onFilesSelected(listOf(it)) }
        }
    }

    return { allowMultiple ->
        multiple = allowMultiple
        val mimeTypes = arrayOf(
            "application/epub+zip",
            "application/pdf",
            "application/zip",
            "application/x-rar-compressed",
            "application/x-cbz",
            "application/x-cbr"
        )
        if (allowMultiple) {
            pickMultiple.launch(mimeTypes)
        } else {
            pickSingle.launch(mimeTypes)
        }
    }
}

/**
 * File picker helper for Activity/ViewModel
 */
class FilePickerHelper(private val activity: Activity) {

    private val pickMultiple = activity.registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        // Handle in callback
    }

    private val pickSingle = activity.registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        // Handle in callback
    }

    fun pickBooks(multiple: Boolean = true, callback: (List<Uri>) -> Unit) {
        val mimeTypes = arrayOf(
            "application/epub+zip",
            "application/pdf",
            "application/zip",
            "application/x-rar-compressed",
            "application/x-cbz",
            "application/x-cbr"
        )
        if (multiple) {
            pickMultiple.launch(mimeTypes) { uris ->
                callback(uris)
            }
        } else {
            pickSingle.launch(mimeTypes) { uri ->
                uri?.let { callback(listOf(it)) }
            }
        }
    }
}

/**
 * Import result handler
 */
sealed interface ImportResult {
    data class Success(val bookIds: List<Long>) : ImportResult
    data class Error(val message: String) : ImportResult
    data class Partial(val bookIds: List<Long>, val errors: List<String>) : ImportResult
}

/**
 * Batch import books from URIs
 */
suspend fun importBooksFromUris(
    context: android.content.Context,
    uris: List<Uri>,
    repository: com.komorebi.reader.data.repository.KomorebiRepository
): ImportResult {
    return kotlinx.coroutines.withContext(Dispatchers.IO) {
        val bookIds = mutableListOf<Long>()
        val errors = mutableListOf<String>()

        for (uri in uris) {
            try {
                val result = com.komorebi.reader.data.parser.BookParser.parseFromUri(context, uri)
                when (result) {
                    is com.komorebi.reader.data.parser.ParseResult.Success -> {
                        val bookId = repository.insertBook(
                            com.komorebi.reader.data.local.entity.BookEntity(
                                sourceId = "local",
                                sourceKey = uri.toString(),
                                title = result.book.title,
                                author = result.book.author,
                                description = result.book.description,
                                coverUrl = null,
                                localCoverPath = null,
                                type = when (result.book.type) {
                                    com.komorebi.reader.data.parser.BookType.CBZ -> com.komorebi.reader.data.local.entity.BookType.Manga
                                    com.komorebi.reader.data.parser.BookType.PDF -> com.komorebi.reader.data.local.entity.BookType.Novel
                                    com.komorebi.reader.data.parser.BookType.EPUB -> com.komorebi.reader.data.local.entity.BookType.Novel
                                    else -> com.komorebi.reader.data.local.entity.BookType.Other
                                },
                                isDownloaded = true,
                                downloadPath = result.book.metadata["tempFile"]
                            )
                        )
                        bookIds.add(bookId)

                        // Insert chapters/pages
                        result.book.pages.forEachIndexed { index, page ->
                            repository.insertChapter(
                                com.komorebi.reader.data.local.entity.ChapterEntity(
                                    bookId = bookId,
                                    sourceChapterId = page.imageUri ?: "page_$index",
                                    title = "Page ${index + 1}",
                                    number = (index + 1).toDouble(),
                                    pageCount = 1,
                                    pagesJson = page.imageUri?.let { com.google.gson.Gson().toJson(listOf(it)) } ?: "[]"
                                )
                            )
                        }
                    }
                    is com.komorebi.reader.data.parser.ParseResult.Error -> {
                        errors.add("${FileUtils.getFileName(context, uri)}: ${result.message}")
                    }
                }
            } catch (e: Exception) {
                errors.add("${FileUtils.getFileName(context, uri)}: ${e.message}")
            }
        }

        when {
            bookIds.isNotEmpty() && errors.isEmpty() -> ImportResult.Success(bookIds)
            bookIds.isNotEmpty() -> ImportResult.Partial(bookIds, errors)
            else -> ImportResult.Error(errors.joinToString("\n") ?: "Aucun livre importé")
        }
    }
}