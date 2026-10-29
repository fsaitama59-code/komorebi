package com.komorebi.reader.ui.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.komorebi.reader.data.repository.KomorebiRepository
import com.komorebi.reader.ui.screens.ExploreScreen.ExploreSource
import com.komorebi.reader.ui.screens.ExploreScreen.ExploreCategory
import com.komorebi.reader.ui.screens.ExploreScreen.ExploreItem

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: KomorebiRepository
) : ViewModel() {

    private val _selectedSource = MutableStateFlow(ExploreSource.MangaDex)
    val selectedSource = _selectedSource.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ExploreCategory.Trending)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _trending = MutableStateFlow<List<ExploreItem>>(emptyList())
    val trending = _trending.asStateFlow()

    private val _newReleases = MutableStateFlow<List<ExploreItem>>(emptyList())
    val newReleases = _newReleases.asStateFlow()

    private val _recommendations = MutableStateFlow<List<ExploreItem>>(emptyList())
    val recommendations = _recommendations.asStateFlow()

    private val _genres = MutableStateFlow<List<String>>(emptyList())
    val genres = _genres.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            // In real app: fetch from selected source API
            loadSampleData()
            _isLoading.value = false
        }
    }

    private fun loadSampleData() {
        // Sample data - in real app from network
        _trending.value = getSampleTrending()
        _newReleases.value = getSampleNewReleases()
        _recommendations.value = getSampleRecommendations()
        _genres.value = getSampleGenres()
    }

    fun setSource(source: ExploreSource) {
        _selectedSource.value = source
        loadData()
    }

    fun setCategory(category: ExploreCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun getSampleTrending(): List<ExploreItem> = listOf(
        ExploreItem("ex1", "Chainsaw Man", "Tatsuki Fujimoto", null, ExploreSource.MangaDex, 4.8f, 150, listOf("Action", "Horror"), listOf()),
        ExploreItem("ex2", "Spy × Family", "Tatsuya Endo", null, ExploreSource.MangaPlus, 4.9f, 120, listOf("Spy", "Comedy"), listOf()),
        ExploreItem("ex3", "Kaiju No. 8", "Naoya Matsumoto", null, ExploreSource.MangaDex, 4.7f, 100, listOf("Action", "Sci-Fi"), listOf()),
        ExploreItem("ex4", "Oshi no Ko", "Aka Akasaka", null, ExploreSource.MangaDex, 4.8f, 140, listOf("Drama", "Mystery"), listOf()),
        ExploreItem("ex5", "Frieren", "Kanehito Yamada", null, ExploreSource.MangaDex, 4.9f, 120, listOf("Fantasy", "Adventure"), listOf()),
        ExploreItem("ex6", "Solo Leveling", "Chugong", null, ExploreSource.Webtoon, 4.8f, 200, listOf("Action", "Fantasy"), listOf())
    )

    private fun getSampleNewReleases(): List<ExploreItem> = listOf(
        ExploreItem("nr1", "Sakamoto Days", "Yuto Suzuki", null, ExploreSource.MangaPlus, 4.6f, 150, listOf("Action", "Comedy"), listOf()),
        ExploreItem("nr2", "Kagurabachi", "Takeru Hokazono", null, ExploreSource.MangaPlus, 4.5f, 30, listOf("Action", "Sword"), listOf()),
        ExploreItem("nr3", "Two Faces", "Hiroki Tachibana", null, ExploreSource.MangaDex, 4.3f, 20, listOf("Mystery", "Psychological"), listOf()),
        ExploreItem("nr4", "Cats of the Louvre", "Taiyo Matsumoto", null, ExploreSource.MangaDex, 4.4f, 10, listOf("Art", "Slice of Life"), listOf())
    )

    private fun getSampleRecommendations(): List<ExploreItem> = listOf(
        ExploreItem("rec1", "Vinland Saga", "Makoto Yukimura", null, ExploreSource.MangaDex, 4.9f, 200, listOf("Historical", "Vikings"), listOf()),
        ExploreItem("rec2", "Monster", "Naoki Urasawa", null, ExploreSource.MangaDex, 4.9f, 162, listOf("Psychological", "Thriller"), listOf()),
        ExploreItem("rec3", "20th Century Boys", "Naoki Urasawa", null, ExploreSource.MangaDex, 4.8f, 220, listOf("Mystery", "Sci-Fi"), listOf()),
        ExploreItem("rec4", "Pluto", "Naoki Urasawa", null, ExploreSource.MangaDex, 4.8f, 65, listOf("Sci-Fi", "Mystery"), listOf())
    )

    private fun getSampleGenres(): List<String> = listOf(
        "Action", "Aventure", "Comédie", "Drame", "Fantastique",
        "Horreur", "Mystère", "Romance", "Sci-Fi", "Slice of Life",
        "Sports", "Supernatural", "Thriller", "Isekai", "Mecha",
        "Historical", "Psychological", "Seinen", "Shonen", "Shojo"
    )
}