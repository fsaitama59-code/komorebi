package com.komorebi.reader.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.komorebi.reader.data.repository.KomorebiRepository
import com.komorebi.reader.data.local.entity.BookEntity
import com.komorebi.reader.data.local.entity.BookType
import com.komorebi.reader.ui.components.GlassComponents.LibraryFilter
import com.komorebi.reader.ui.components.GlassComponents.ViewMode

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: KomorebiRepository
) : ViewModel() {

    private val _books = MutableStateFlow<List<BookEntity>>(emptyList())
    val books = _books.asStateFlow()

    private val _filteredBooks = MutableStateFlow<List<BookEntity>>(emptyList())
    val filteredBooks = _filteredBooks.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow(LibraryFilter.All)
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _viewMode = MutableStateFlow(ViewMode.Grid)
    val viewMode = _viewMode.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllBooks()
                .onEach { books ->
                    _books.value = books
                    applyFilters()
                }
                .launchIn(viewModelScope)
            _isLoading.value = false
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun setFilter(filter: LibraryFilter) {
        _selectedFilter.value = filter
        applyFilters()
    }

    fun setViewMode(mode: ViewMode) {
        _viewMode.value = mode
    }

    private fun applyFilters() {
        var result = _books.value
        val query = _searchQuery.value.lowercase()
        val filter = _selectedFilter.value

        if (query.isNotBlank()) {
            result = result.filter { it.title.lowercase().contains(query) || it.author.lowercase().contains(query) }
        }

        if (filter != LibraryFilter.All) {
            result = result.filter { it.type == filter.toBookType() }
        }

        _filteredBooks.value = result
    }

    fun openReader(book: BookEntity) {
        // Navigation handled by UI
    }
}

private fun LibraryFilter.toBookType(): BookType = when (this) {
    LibraryFilter.Manga -> BookType.Manga
    LibraryFilter.Novel -> BookType.Novel
    LibraryFilter.Comic -> BookType.Comic
    LibraryFilter.Magazine -> BookType.Magazine
    else -> BookType.Manga
}