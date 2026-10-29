package com.komorebi.reader.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.komorebi.reader.data.local.dao.BookDao
import com.komorebi.reader.data.local.dao.ChapterDao
import com.komorebi.reader.data.local.dao.ReadingProgressDao
import com.komorebi.reader.data.local.dao.SourceDao
import com.komorebi.reader.data.local.dao.BookmarkDao
import com.komorebi.reader.data.local.dao.LibraryScanDao
import com.komorebi.reader.data.local.entity.BookEntity
import com.komorebi.reader.data.local.entity.ChapterEntity
import com.komorebi.reader.data.local.entity.ReadingProgressEntity
import com.komorebi.reader.data.local.entity.SourceEntity
import com.komorebi.reader.data.local.entity.BookmarkEntity
import com.komorebi.reader.data.local.entity.LibraryScanEntity
import com.komorebi.reader.data.local.entity.BookWithChapters
import com.komorebi.reader.data.preferences.PreferencesRepository
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

class KomorebiRepository @Inject constructor(
    private val bookDao: BookDao,
    private val chapterDao: ChapterDao,
    private val progressDao: ReadingProgressDao,
    private val sourceDao: SourceDao,
    private val bookmarkDao: BookmarkDao,
    private val scanDao: LibraryScanDao,
    private val prefs: PreferencesRepository
) {

    // ===== BOOKS =====

    fun getAllBooks(): Flow<List<BookEntity>> = bookDao.getAll()

    fun getBooksBySource(sourceId: String): Flow<List<BookEntity>> = bookDao.getBySource(sourceId)

    fun getBooksByType(type: com.komorebi.reader.data.local.entity.BookType): Flow<List<BookEntity>> =
        bookDao.getByType(type)

    fun getFavorites(): Flow<List<BookEntity>> = bookDao.getFavorites()

    fun getDownloaded(): Flow<List<BookEntity>> = bookDao.getDownloaded()

    fun getRecentlyRead(limit: Int = 20): Flow<List<BookEntity>> = bookDao.getRecentlyRead(limit)

    fun getRecentlyAdded(limit: Int = 20): Flow<List<BookEntity>> = bookDao.getRecentlyAdded(limit)

    fun searchBooks(query: String): Flow<List<BookEntity>> = bookDao.search("%$query%")

    fun getBooksSorted(sortOrder: String): Flow<List<BookEntity>> = bookDao.getAllSorted(sortOrder)

    fun getBookById(id: Long): Flow<BookEntity?> = bookDao.getById(id)

    fun getBookWithChapters(bookId: Long): Flow<BookWithChapters?> = bookDao.getBookWithChapters(bookId)

    suspend fun insertBook(book: BookEntity): Long = bookDao.insert(book)

    suspend fun insertBooks(books: List<BookEntity>): List<Long> = bookDao.insertAll(books)

    suspend fun updateBook(book: BookEntity) = bookDao.update(book)

    suspend fun deleteBook(id: Long) = bookDao.deleteById(id)

    suspend fun updateReadingProgress(bookId: Long, progress: Float, chapterId: Long, page: Int) {
        val now = Clock.System.now().toString()
        bookDao.updateReadingProgress(bookId, progress, chapterId, page, now)
    }

    suspend fun setFavorite(bookId: Long, favorite: Boolean) = bookDao.setFavorite(bookId, favorite)

    suspend fun setDownloaded(bookId: Long, downloaded: Boolean, path: String?) =
        bookDao.setDownloaded(bookId, downloaded, path)

    // ===== CHAPTERS =====

    fun getChaptersByBook(bookId: Long): Flow<List<ChapterEntity>> = chapterDao.getByBookId(bookId)

    fun getFirstUnreadChapter(bookId: Long): Flow<ChapterEntity?> = chapterDao.getFirstUnread(bookId)

    fun getNextChapter(bookId: Long, currentNumber: Double): Flow<ChapterEntity?> =
        chapterDao.getNextChapter(bookId, currentNumber)

    fun getPreviousChapter(bookId: Long, currentNumber: Double): Flow<ChapterEntity?> =
        chapterDao.getPreviousChapter(bookId, currentNumber)

    suspend fun insertChapter(chapter: ChapterEntity): Long = chapterDao.insert(chapter)

    suspend fun insertChapters(chapters: List<ChapterEntity>): List<Long> = chapterDao.insertAll(chapters)

    suspend fun updateChapter(chapter: ChapterEntity) = chapterDao.update(chapter)

    suspend fun markChapterAsRead(chapterId: Long) {
        val now = Clock.System.now().toString()
        chapterDao.markAsRead(chapterId, now)
    }

    suspend fun setChapterDownloaded(chapterId: Long, path: String) {
        val now = Clock.System.now().toString()
        chapterDao.setDownloaded(chapterId, path, now)
    }

    // ===== READING PROGRESS =====

    fun getReadingProgress(bookId: Long, chapterId: Long): Flow<ReadingProgressEntity?> =
        progressDao.getProgress(bookId, chapterId)

    fun getLatestProgress(bookId: Long): Flow<ReadingProgressEntity?> = progressDao.getLatestProgress(bookId)

    suspend fun saveReadingProgress(progress: ReadingProgressEntity) = progressDao.insert(progress)

    // ===== SOURCES =====

    fun getEnabledSources(): Flow<List<SourceEntity>> = sourceDao.getEnabledSources()

    fun getAllSources(): Flow<List<SourceEntity>> = sourceDao.getAllSources()

    suspend fun setSourceEnabled(sourceId: String, enabled: Boolean) = sourceDao.setEnabled(sourceId, enabled)

    suspend fun setSourcePriority(sourceId: String, priority: Int) = sourceDao.setPriority(sourceId, priority)

    suspend fun setSourceAuthToken(sourceId: String, token: String?) = sourceDao.setAuthToken(sourceId, token)

    // ===== BOOKMARKS =====

    fun getBookmarksByBook(bookId: Long): Flow<List<BookmarkEntity>> = bookmarkDao.getByBookId(bookId)

    fun getBookmarksByChapter(chapterId: Long): Flow<List<BookmarkEntity>> = bookmarkDao.getByChapterId(chapterId)

    suspend fun addBookmark(bookmark: BookmarkEntity) = bookmarkDao.insert(bookmark)

    suspend fun updateBookmark(bookmark: BookmarkEntity) = bookmarkDao.update(bookmark)

    suspend fun deleteBookmark(id: Long) = bookmarkDao.deleteById(id)

    // ===== LIBRARY SCANS =====

    fun getLatestScan(): Flow<LibraryScanEntity?> = scanDao.getLatestScan()

    fun getRecentScans(limit: Int = 10): Flow<List<LibraryScanEntity>> = scanDao.getRecentScans(limit)

    suspend fun startLibraryScan(rootPath: String): Long {
        val scan = LibraryScanEntity(
            rootPath = rootPath,
            status = com.komorebi.reader.data.local.entity.ScanStatus.Running
        )
        return scanDao.insert(scan)
    }

    suspend fun updateScanProgress(
        scanId: Long,
        status: com.komorebi.reader.data.local.entity.ScanStatus,
        processed: Int,
        newBooks: Int,
        updated: Int,
        errors: Int,
        errorLog: String
    ) = scanDao.updateProgress(scanId, status, processed, newBooks, updated, errors, errorLog)

    suspend fun completeScan(scanId: Long, status: com.komorebi.reader.data.local.entity.ScanStatus) {
        val now = Clock.System.now().toString()
        scanDao.completeScan(scanId, status, now)
    }

    // ===== PREFERENCES (delegated) =====

    val themeMode = prefs.themeMode
    val readingDirection = prefs.readingDirection
    val readingTheme = prefs.readingTheme
    val fontSize = prefs.fontSize
    val lineHeight = prefs.lineHeight
    val margins = prefs.margins
    val keepScreenOn = prefs.keepScreenOn
    val autoAdvance = prefs.autoAdvance
    val swipeToTurn = prefs.swipeToTurn
    val doubleTapZoom = prefs.doubleTapZoom
    val showProgress = prefs.showProgress
    val groupBySeries = prefs.groupBySeries
    val showCovers = prefs.showCovers
    val sortOrder = prefs.sortOrder
    val syncEnabled = prefs.syncEnabled
    val wifiOnlySync = prefs.wifiOnlySync
    val autoBackup = prefs.autoBackup
    val autoNightMode = prefs.autoNightMode
    val accentColor = prefs.accentColor
    val deviceId = prefs.deviceId
    val isFirstLaunch = prefs.isFirstLaunch
    val libraryPath = prefs.libraryPath
    val lastSync = prefs.lastSync

    suspend fun setThemeMode(mode: com.komorebi.reader.ui.navigation.ThemeMode) = prefs.setThemeMode(mode)
    suspend fun setReadingDirection(dir: String) = prefs.setReadingDirection(dir)
    suspend fun setReadingTheme(theme: String) = prefs.setReadingTheme(theme)
    suspend fun setFontSize(size: String) = prefs.setFontSize(size)
    suspend fun setLineHeight(height: String) = prefs.setLineHeight(height)
    suspend fun setMargins(margin: String) = prefs.setMargins(margin)
    suspend fun setKeepScreenOn(enabled: Boolean) = prefs.setKeepScreenOn(enabled)
    suspend fun setAutoAdvance(enabled: Boolean) = prefs.setAutoAdvance(enabled)
    suspend fun setSwipeToTurn(enabled: Boolean) = prefs.setSwipeToTurn(enabled)
    suspend fun setDoubleTapZoom(enabled: Boolean) = prefs.setDoubleTapZoom(enabled)
    suspend fun setShowProgress(enabled: Boolean) = prefs.setShowProgress(enabled)
    suspend fun setGroupBySeries(enabled: Boolean) = prefs.setGroupBySeries(enabled)
    suspend fun setShowCovers(enabled: Boolean) = prefs.setShowCovers(enabled)
    suspend fun setSortOrder(order: String) = prefs.setSortOrder(order)
    suspend fun setSyncEnabled(enabled: Boolean) = prefs.setSyncEnabled(enabled)
    suspend fun setWifiOnlySync(enabled: Boolean) = prefs.setWifiOnlySync(enabled)
    suspend fun setAutoBackup(enabled: Boolean) = prefs.setAutoBackup(enabled)
    suspend fun setAutoNightMode(enabled: Boolean) = prefs.setAutoNightMode(enabled)
    suspend fun setAccentColor(color: String) = prefs.setAccentColor(color)
    suspend fun setFirstLaunchComplete() = prefs.setFirstLaunchComplete()
    suspend fun setLibraryPath(path: String?) = prefs.setLibraryPath(path)
    suspend fun setLastSync(timestamp: Long) = prefs.setLastSync(timestamp)
}