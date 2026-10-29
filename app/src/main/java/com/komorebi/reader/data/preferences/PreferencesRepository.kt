package com.komorebi.reader.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesKeys
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.rxjava3.RxDataStore
import androidx.datastore.preferences.rxjava3.RxDataStoreBuilder
import androidx.datastore.preferences.rxjava3.preferencesDataStore
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.tasks.await
import com.komorebi.reader.ui.navigation.ThemeMode

class PreferencesRepository(private val context: Context) {

    private val dataStore: RxDataStore<Preferences> = RxDataStoreBuilder(context, "komorebi_prefs").build()

    // Keys
    private val THEME_MODE = PreferencesKeys.stringKey("theme_mode")
    private val READING_DIRECTION = PreferencesKeys.stringKey("reading_direction")
    private val READING_THEME = PreferencesKeys.stringKey("reading_theme")
    private val FONT_SIZE = PreferencesKeys.stringKey("font_size")
    private val LINE_HEIGHT = PreferencesKeys.stringKey("line_height")
    private val MARGINS = PreferencesKeys.stringKey("margins")
    private val KEEP_SCREEN_ON = PreferencesKeys.booleanKey("keep_screen_on")
    private val AUTO_ADVANCE = PreferencesKeys.booleanKey("auto_advance")
    private val SWIPE_TO_TURN = PreferencesKeys.booleanKey("swipe_to_turn")
    private val DOUBLE_TAP_ZOOM = PreferencesKeys.booleanKey("double_tap_zoom")
    private val SHOW_PROGRESS = PreferencesKeys.booleanKey("show_progress")
    private val GROUP_BY_SERIES = PreferencesKeys.booleanKey("group_by_series")
    private val SHOW_COVERS = PreferencesKeys.booleanKey("show_covers")
    private val SORT_ORDER = PreferencesKeys.stringKey("sort_order")
    private val SYNC_ENABLED = PreferencesKeys.booleanKey("sync_enabled")
    private val WIFI_ONLY_SYNC = PreferencesKeys.booleanKey("wifi_only_sync")
    private val AUTO_BACKUP = PreferencesKeys.booleanKey("auto_backup")
    private val AUTO_NIGHT_MODE = PreferencesKeys.booleanKey("auto_night_mode")
    private val ACCENT_COLOR = PreferencesKeys.stringKey("accent_color")
    private val DEVICE_ID = PreferencesKeys.stringKey("device_id")
    private val FIRST_LAUNCH = PreferencesKeys.booleanKey("first_launch")
    private val LIBRARY_PATH = PreferencesKeys.stringKey("library_path")
    private val LAST_SYNC = PreferencesKeys.longKey("last_sync")

    // Theme Mode
    val themeMode: Flowable<ThemeMode> = dataStore.data
        .map { prefs -> ThemeMode.valueOf(prefs[THEME_MODE] ?: ThemeMode.System.name) }
        .distinctUntilChanged()

    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.updateDataAsync { it.edit().putString(THEME_MODE, mode.name).apply() }.await()
    }

    // Reading Direction
    val readingDirection: Flowable<String> = dataStore.data
        .map { prefs -> prefs[READING_DIRECTION] ?: "rtl" }
        .distinctUntilChanged()

    suspend fun setReadingDirection(direction: String) {
        dataStore.updateDataAsync { it.edit().putString(READING_DIRECTION, direction).apply() }.await()
    }

    // Reading Theme
    val readingTheme: Flowable<String> = dataStore.data
        .map { prefs -> prefs[READING_THEME] ?: "dark" }
        .distinctUntilChanged()

    suspend fun setReadingTheme(theme: String) {
        dataStore.updateDataAsync { it.edit().putString(READING_THEME, theme).apply() }.await()
    }

    // Font Size
    val fontSize: Flowable<String> = dataStore.data
        .map { prefs -> prefs[FONT_SIZE] ?: "medium" }
        .distinctUntilChanged()

    suspend fun setFontSize(size: String) {
        dataStore.updateDataAsync { it.edit().putString(FONT_SIZE, size).apply() }.await()
    }

    // Line Height
    val lineHeight: Flowable<String> = dataStore.data
        .map { prefs -> prefs[LINE_HEIGHT] ?: "normal" }
        .distinctUntilChanged()

    suspend fun setLineHeight(height: String) {
        dataStore.updateDataAsync { it.edit().putString(LINE_HEIGHT, height).apply() }.await()
    }

    // Margins
    val margins: Flowable<String> = dataStore.data
        .map { prefs -> prefs[MARGINS] ?: "normal" }
        .distinctUntilChanged()

    suspend fun setMargins(margin: String) {
        dataStore.updateDataAsync { it.edit().putString(MARGINS, margin).apply() }.await()
    }

    // Boolean settings
    val keepScreenOn: Flowable<Boolean> = dataStore.data
        .map { prefs -> prefs[KEEP_SCREEN_ON] ?: true }
        .distinctUntilChanged()

    suspend fun setKeepScreenOn(enabled: Boolean) {
        dataStore.updateDataAsync { it.edit().putBoolean(KEEP_SCREEN_ON, enabled).apply() }.await()
    }

    val autoAdvance: Flowable<Boolean> = dataStore.data
        .map { prefs -> prefs[AUTO_ADVANCE] ?: false }
        .distinctUntilChanged()

    suspend fun setAutoAdvance(enabled: Boolean) {
        dataStore.updateDataAsync { it.edit().putBoolean(AUTO_ADVANCE, enabled).apply() }.await()
    }

    val swipeToTurn: Flowable<Boolean> = dataStore.data
        .map { prefs -> prefs[SWIPE_TO_TURN] ?: true }
        .distinctUntilChanged()

    suspend fun setSwipeToTurn(enabled: Boolean) {
        dataStore.updateDataAsync { it.edit().putBoolean(SWIPE_TO_TURN, enabled).apply() }.await()
    }

    val doubleTapZoom: Flowable<Boolean> = dataStore.data
        .map { prefs -> prefs[DOUBLE_TAP_ZOOM] ?: true }
        .distinctUntilChanged()

    suspend fun setDoubleTapZoom(enabled: Boolean) {
        dataStore.updateDataAsync { it.edit().putBoolean(DOUBLE_TAP_ZOOM, enabled).apply() }.await()
    }

    val showProgress: Flowable<Boolean> = dataStore.data
        .map { prefs -> prefs[SHOW_PROGRESS] ?: true }
        .distinctUntilChanged()

    suspend fun setShowProgress(enabled: Boolean) {
        dataStore.updateDataAsync { it.edit().putBoolean(SHOW_PROGRESS, enabled).apply() }.await()
    }

    val groupBySeries: Flowable<Boolean> = dataStore.data
        .map { prefs -> prefs[GROUP_BY_SERIES] ?: true }
        .distinctUntilChanged()

    suspend fun setGroupBySeries(enabled: Boolean) {
        dataStore.updateDataAsync { it.edit().putBoolean(GROUP_BY_SERIES, enabled).apply() }.await()
    }

    val showCovers: Flowable<Boolean> = dataStore.data
        .map { prefs -> prefs[SHOW_COVERS] ?: true }
        .distinctUntilChanged()

    suspend fun setShowCovers(enabled: Boolean) {
        dataStore.updateDataAsync { it.edit().putBoolean(SHOW_COVERS, enabled).apply() }.await()
    }

    val sortOrder: Flowable<String> = dataStore.data
        .map { prefs -> prefs[SORT_ORDER] ?: "recentlyRead" }
        .distinctUntilChanged()

    suspend fun setSortOrder(order: String) {
        dataStore.updateDataAsync { it.edit().putString(SORT_ORDER, order).apply() }.await()
    }

    // Sync settings
    val syncEnabled: Flowable<Boolean> = dataStore.data
        .map { prefs -> prefs[SYNC_ENABLED] ?: false }
        .distinctUntilChanged()

    suspend fun setSyncEnabled(enabled: Boolean) {
        dataStore.updateDataAsync { it.edit().putBoolean(SYNC_ENABLED, enabled).apply() }.await()
    }

    val wifiOnlySync: Flowable<Boolean> = dataStore.data
        .map { prefs -> prefs[WIFI_ONLY_SYNC] ?: true }
        .distinctUntilChanged()

    suspend fun setWifiOnlySync(enabled: Boolean) {
        dataStore.updateDataAsync { it.edit().putBoolean(WIFI_ONLY_SYNC, enabled).apply() }.await()
    }

    val autoBackup: Flowable<Boolean> = dataStore.data
        .map { prefs -> prefs[AUTO_BACKUP] ?: false }
        .distinctUntilChanged()

    suspend fun setAutoBackup(enabled: Boolean) {
        dataStore.updateDataAsync { it.edit().putBoolean(AUTO_BACKUP, enabled).apply() }.await()
    }

    val autoNightMode: Flowable<Boolean> = dataStore.data
        .map { prefs -> prefs[AUTO_NIGHT_MODE] ?: true }
        .distinctUntilChanged()

    suspend fun setAutoNightMode(enabled: Boolean) {
        dataStore.updateDataAsync { it.edit().putBoolean(AUTO_NIGHT_MODE, enabled).apply() }.await()
    }

    // Accent color
    val accentColor: Flowable<String> = dataStore.data
        .map { prefs -> prefs[ACCENT_COLOR] ?: "#2EB88A" }
        .distinctUntilChanged()

    suspend fun setAccentColor(color: String) {
        dataStore.updateDataAsync { it.edit().putString(ACCENT_COLOR, color).apply() }.await()
    }

    // Device ID (for sync)
    val deviceId: Single<String> = dataStore.data
        .firstOrError()
        .map { prefs ->
            prefs[DEVICE_ID] ?: java.util.UUID.randomUUID().toString().also { id ->
                dataStore.updateDataAsync { it.edit().putString(DEVICE_ID, id).apply() }.await()
            }
        }

    // First launch
    val isFirstLaunch: Flowable<Boolean> = dataStore.data
        .map { prefs -> prefs[FIRST_LAUNCH] ?: true }
        .distinctUntilChanged()

    suspend fun setFirstLaunchComplete() {
        dataStore.updateDataAsync { it.edit().putBoolean(FIRST_LAUNCH, false).apply() }.await()
    }

    // Library path
    val libraryPath: Flowable<String?> = dataStore.data
        .map { prefs -> prefs[LIBRARY_PATH] }
        .distinctUntilChanged()

    suspend fun setLibraryPath(path: String?) {
        dataStore.updateDataAsync { prefs ->
            val editor = prefs.edit()
            path?.let { editor.putString(LIBRARY_PATH, it) } ?: editor.remove(LIBRARY_PATH)
            editor.apply()
        }.await()
    }

    // Last sync timestamp
    val lastSync: Flowable<Long> = dataStore.data
        .map { prefs -> prefs[LAST_SYNC] ?: 0L }
        .distinctUntilChanged()

    suspend fun setLastSync(timestamp: Long) {
        dataStore.updateDataAsync { it.edit().putLong(LAST_SYNC, timestamp).apply() }.await()
    }

    // Reset all preferences
    suspend fun resetAll() {
        dataStore.updateDataAsync { prefs ->
            prefs.edit().clear().apply()
        }.await()
    }
}