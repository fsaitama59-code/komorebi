package com.komorebi.reader.ui.screens.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.komorebi.reader.data.repository.KomorebiRepository
import com.komorebi.reader.data.local.entity.ChapterEntity
import com.komorebi.reader.data.local.entity.ReadingProgressEntity
import com.komorebi.reader.ui.screens.ReaderScreen.ReadingDirection
import com.komorebi.reader.ui.screens.ReaderScreen.ReaderTheme

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val repository: KomorebiRepository
) : ViewModel() {

    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    private val _zoomLevel = MutableStateFlow(1.0f)
    val zoomLevel = _zoomLevel.asStateFlow()

    private val _readingDirection = MutableStateFlow(ReadingDirection.RightToLeft)
    val readingDirection = _readingDirection.asStateFlow()

    private val _theme = MutableStateFlow(ReaderTheme.Dark)
    val theme = _theme.asStateFlow()

    private val _pages = MutableStateFlow<List<String>>(emptyList())
    val pages = _pages.asStateFlow()

    private val _chapter = MutableStateFlow<ChapterEntity?>(null)
    val chapter = _chapter.asStateFlow()

    private val _showUI = MutableStateFlow(true)
    val showUI = _showUI.asStateFlow()

    fun setChapter(chapter: ChapterEntity, bookId: Long) {
        _chapter.value = chapter
        // Load pages from chapter.pagesJson
        // In real app: parse JSON and load from local/network
        _pages.value = (1..chapter.pageCount).map { "page_$it.jpg" }
        loadProgress(bookId, chapter.id)
    }

    private fun loadProgress(bookId: Long, chapterId: Long) {
        viewModelScope.launch {
            repository.getReadingProgress(bookId, chapterId)
                .onEach { progress ->
                    progress?.let {
                        _currentPage.value = it.pageIndex
                        _zoomLevel.value = it.zoomLevel
                        _readingDirection.value = ReadingDirection.valueOf(it.readingDirection.uppercase())
                        _theme.value = ReaderTheme.valueOf(it.theme.capitalize())
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    fun setCurrentPage(page: Int) {
        _currentPage.value = page
    }

    fun setZoomLevel(zoom: Float) {
        _zoomLevel.value = zoom.coerceIn(0.5f, 4f)
    }

    fun setReadingDirection(dir: ReadingDirection) {
        _readingDirection.value = dir
    }

    fun setTheme(t: ReaderTheme) {
        _theme.value = t
    }

    fun toggleUI() {
        _showUI.value = !_showUI.value
    }

    fun saveProgress(bookId: Long, chapterId: Long) {
        val progress = ReadingProgressEntity(
            bookId = bookId,
            chapterId = chapterId,
            pageIndex = _currentPage.value,
            totalPages = _pages.value.size,
            progress = _currentPage.value.toFloat() / _pages.value.size,
            overallProgress = 0f, // Calculate from book
            readingDirection = _readingDirection.value.name.lowercase(),
            zoomLevel = _zoomLevel.value,
            theme = _theme.value.name.lowercase(),
            updatedAt = kotlinx.datetime.Clock.System.now()
        )
        viewModelScope.launch {
            repository.saveReadingProgress(progress)
        }
    }

    fun nextChapter() {
        // Navigate to next chapter
    }

    fun previousChapter() {
        // Navigate to previous chapter
    }
}