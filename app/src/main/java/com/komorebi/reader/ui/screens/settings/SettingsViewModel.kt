package com.komorebi.reader.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.komorebi.reader.data.repository.KomorebiRepository
import com.komorebi.reader.ui.navigation.ThemeMode
import com.komorebi.reader.ui.screens.ReaderScreen.ReadingDirection
import com.komorebi.reader.ui.screens.ReaderScreen.ReaderTheme
import com.komorebi.reader.ui.screens.SettingsScreen.*

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: KomorebiRepository
) : ViewModel() {

    // Appearance
    val themeMode = repository.themeMode
    val autoNightMode = repository.autoNightMode
    val accentColor = repository.accentColor

    // Reading
    val readingDirection = repository.readingDirection
    val readingTheme = repository.readingTheme
    val fontSize = repository.fontSize
    val lineHeight = repository.lineHeight
    val margins = repository.margins

    // Reading behavior
    val keepScreenOn = repository.keepScreenOn
    val autoAdvance = repository.autoAdvance
    val swipeToTurn = repository.swipeToTurn
    val doubleTapZoom = repository.doubleTapZoom
    val showProgress = repository.showProgress

    // Library
    val groupBySeries = repository.groupBySeries
    val showCovers = repository.showCovers
    val sortOrder = repository.sortOrder

    // Sync
    val syncEnabled = repository.syncEnabled
    val wifiOnlySync = repository.wifiOnlySync
    val autoBackup = repository.autoBackup

    // Data
    val deviceId = repository.deviceId
    val isFirstLaunch = repository.isFirstLaunch
    val libraryPath = repository.libraryPath
    val lastSync = repository.lastSync

    // Setters
    suspend fun setThemeMode(mode: ThemeMode) = repository.setThemeMode(mode)
    suspend fun setAutoNightMode(enabled: Boolean) = repository.setAutoNightMode(enabled)
    suspend fun setAccentColor(color: String) = repository.setAccentColor(color)

    suspend fun setReadingDirection(dir: String) = repository.setReadingDirection(dir)
    suspend fun setReadingTheme(theme: String) = repository.setReadingTheme(theme)
    suspend fun setFontSize(size: String) = repository.setFontSize(size)
    suspend fun setLineHeight(height: String) = repository.setLineHeight(height)
    suspend fun setMargins(margin: String) = repository.setMargins(margin)

    suspend fun setKeepScreenOn(enabled: Boolean) = repository.setKeepScreenOn(enabled)
    suspend fun setAutoAdvance(enabled: Boolean) = repository.setAutoAdvance(enabled)
    suspend fun setSwipeToTurn(enabled: Boolean) = repository.setSwipeToTurn(enabled)
    suspend fun setDoubleTapZoom(enabled: Boolean) = repository.setDoubleTapZoom(enabled)
    suspend fun setShowProgress(enabled: Boolean) = repository.setShowProgress(enabled)

    suspend fun setGroupBySeries(enabled: Boolean) = repository.setGroupBySeries(enabled)
    suspend fun setShowCovers(enabled: Boolean) = repository.setShowCovers(enabled)
    suspend fun setSortOrder(order: String) = repository.setSortOrder(order)

    suspend fun setSyncEnabled(enabled: Boolean) = repository.setSyncEnabled(enabled)
    suspend fun setWifiOnlySync(enabled: Boolean) = repository.setWifiOnlySync(enabled)
    suspend fun setAutoBackup(enabled: Boolean) = repository.setAutoBackup(enabled)

    suspend fun setFirstLaunchComplete() = repository.setFirstLaunchComplete()
    suspend fun setLibraryPath(path: String?) = repository.setLibraryPath(path)
    suspend fun setLastSync(timestamp: Long) = repository.setLastSync(timestamp)

    // Actions
    suspend fun clearCache() {
        // Clear Coil cache, image cache, etc.
    }

    suspend fun exportLibrary() {
        // Export to JSON/CSV
    }

    suspend fun importLibrary() {
        // Import from backup
    }

    suspend fun resetApp() {
        repository.setFirstLaunchComplete()
        // Clear database, preferences, etc.
    }

    suspend fun syncNow() {
        repository.setLastSync(System.currentTimeMillis())
        // Trigger sync worker
    }
}