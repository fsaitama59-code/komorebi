package com.komorebi.reader.ui.screens

import androidx.compose.animation.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.drawRect
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.px
import androidx.compose.ui.unit.sp
import com.komorebi.reader.ui.components.GlassComponents.GlassSurface
import com.komorebi.reader.ui.components.GlassComponents.LiquidButton
import com.komorebi.reader.ui.components.GlassComponents.LiquidProgress
import com.komorebi.reader.ui.theme.KomorebiMotion
import com.komorebi.reader.ui.theme.KomorebiSpacing
import com.komorebi.reader.ui.theme.KomorebiTypeScale
import com.komorebi.reader.ui.theme.komorebiColors
import com.komorebi.reader.ui.theme.komorebiShapes
import com.komorebi.reader.ui.theme.komorebiTypography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Reader Screen - Premium immersive reading experience
 * Liquid page turns, glass UI overlays, gesture-driven navigation
 */

@Composable
fun ReaderScreen(
    bookId: String,
    chapterId: String,
    onNavigateUp: () -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    // Reading state
    var showUI by remember { mutableStateOf(true) }
    var showChapterList by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var currentPage by remember { mutableStateOf(0) }
    var zoomLevel by remember { mutableStateOf(1.0f) }
    var readingDirection by remember { mutableStateOf(ReadingDirection.Vertical) }
    var themeMode by remember { mutableStateOf(ReaderTheme.Dark) }

    // Auto-hide UI
    LaunchedEffect(showUI) {
        if (showUI) {
            delay(3000)
            showUI = false
        }
    }

    // Sample chapter data
    val chapter = remember { getSampleChapter(chapterId) }
    val pages = chapter.pages

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        SurfaceBackground(themeMode = themeMode)

        // Pages container
        PageView(
            pages = pages,
            currentPage = currentPage,
            onPageChange = { currentPage = it },
            zoomLevel = zoomLevel,
            readingDirection = readingDirection,
            onTap = { showUI = !showUI },
            onDoubleTap = { zoomLevel = if (zoomLevel > 1f) 1f else 2f },
            onSwipe = { direction ->
                when (direction) {
                    SwipeDirection.Left -> if (currentPage < pages.lastIndex) currentPage++
                    SwipeDirection.Right -> if (currentPage > 0) currentPage--
                    SwipeDirection.Up -> { /* Next chapter */ }
                    SwipeDirection.Down -> { /* Previous chapter */ }
                }
            }
        )

        // UI Overlays
        if (showUI) {
            // Top bar
            ReaderTopBar(
                title = chapter.title,
                progress = (currentPage + 1).toFloat() / pages.size,
                onBack = onNavigateUp,
                onChapterList = { showChapterList = true },
                onSettings = { showSettings = true },
                themeMode = themeMode,
                onThemeChange = { themeMode = it }
            )

            // Bottom bar
            ReaderBottomBar(
                currentPage = currentPage,
                totalPages = pages.size,
                onPageChange = { currentPage = it },
                onPreviousChapter = { /* Navigate */ },
                onNextChapter = { /* Navigate */ }
            )
        }

        // Chapter list sheet
        if (showChapterList) {
            ChapterListSheet(
                chapters = getSampleChapters(),
                currentChapter = chapterId,
                onSelect = { id ->
                    showChapterList = false
                    // Navigate to chapter
                },
                onDismiss = { showChapterList = false }
            )
        }

        // Settings sheet
        if (showSettings) {
            ReaderSettingsSheet(
                readingDirection = readingDirection,
                onDirectionChange = { readingDirection = it },
                themeMode = themeMode,
                onThemeChange = { themeMode = it },
                zoomLevel = zoomLevel,
                onZoomChange = { zoomLevel = it },
                onDismiss = { showSettings = false }
            )
        }
    }
}

// ===== BACKGROUND =====

@Composable
fun SurfaceBackground(themeMode: ReaderTheme) {
    val colors = komorebiColors()

    Box(modifier = Modifier.fillMaxSize()) {
        when (themeMode) {
            ReaderTheme.Dark -> {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        brush = ShaderBrush(
                            androidx.compose.ui.graphics.LinearGradient(
                                colors = listOf(
                                    colors.surfaceBase,
                                    colors.surfaceElevated,
                                    colors.surfaceOverlay
                                ),
                                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                end = androidx.compose.ui.geometry.Offset(0f, size.height)
                            )
                        )
                    )
                }
            }
            ReaderTheme.Sepia -> {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        brush = ShaderBrush(
                            androidx.compose.ui.graphics.LinearGradient(
                                colors = listOf(
                                    Color(0xFFF4ECD8),
                                    Color(0xFFE8DCC0),
                                    Color(0xFFDCC8A8)
                                ),
                                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                end = androidx.compose.ui.geometry.Offset(0f, size.height)
                            )
                        )
                    )
                }
            }
            ReaderTheme.Light -> {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        brush = ShaderBrush(
                            androidx.compose.ui.graphics.LinearGradient(
                                colors = listOf(
                                    Color(0xFFFAFAFA),
                                    Color(0xFFF0F0F0),
                                    Color(0xFFE8E8E8)
                                ),
                                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                end = androidx.compose.ui.geometry.Offset(0f, size.height)
                            )
                        )
                    )
                }
            }
        }
    }
}

// ===== PAGE VIEW =====

@Composable
fun PageView(
    pages: List<PageImage>,
    currentPage: Int,
    onPageChange: (Int) -> Unit,
    zoomLevel: Float,
    readingDirection: ReadingDirection,
    onTap: () -> Unit,
    onDoubleTap: () -> Unit,
    onSwipe: (SwipeDirection) -> Unit
) {
    val colors = komorebiColors()
    val shapes = komorebiShapes()

    val page = pages[currentPage]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTap() },
                    onDoubleTap = { onDoubleTap() },
                    onLongPress = { /* Show context menu */ },
                    onPress = { /* Press feedback */ }
                )
                detectTransformGestures(
                    onGesture = { _, _, zoom, _ ->
                        // Handle pinch zoom
                    }
                )
            }
    ) {
        // Page image
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = zoomLevel
                    scaleY = zoomLevel
                },
            contentAlignment = Alignment.Center
        ) {
            // In real app: Coil AsyncImage with page.url
            Box(
                modifier = Modifier
                    .size(600.dp, 800.dp)
                    .background(colors.surfaceOverlay, shapes.readerPage)
            ) {
                // Page content would be rendered here
                Text(
                    text = "Page ${currentPage + 1}",
                    style = komorebiTypography().readingOverlayLarge.copy(color = colors.contentTertiary)
                )
            }
        }

        // Page turn indicators
        if (currentPage > 0) {
            PageTurnIndicator(
                modifier = Modifier
                    .align(Alignment.CenterLeft)
                    .padding(spacing.Medium),
                direction = PageTurnDirection.Left
            )
        }
        if (currentPage < pages.lastIndex) {
            PageTurnIndicator(
                modifier = Modifier
                    .align(Alignment.CenterRight)
                    .padding(spacing.Medium),
                direction = PageTurnDirection.Right
            )
        }
    }
}

@Composable
fun PageTurnIndicator(
    modifier: Modifier,
    direction: PageTurnDirection
) {
    val colors = komorebiColors()
    val shapes = komorebiShapes()

    GlassSurface(
        modifier = modifier
            .size(48.dp)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {}, onRelease = { /* Turn page */ }, onCancel = {})
            },
        variant = com.komorebi.reader.ui.components.GlassComponents.GlassVariant.Standard,
        shape = shapes.pill
    ) {
        Icon(
            painter = painterResource(when (direction) {
                PageTurnDirection.Left -> android.R.drawable.ic_media_previous
                PageTurnDirection.Right -> android.R.drawable.ic_media_next
            }),
            contentDescription = when (direction) {
                PageTurnDirection.Left -> "Page précédente"
                PageTurnDirection.Right -> "Page suivante"
            },
            tint = colors.contentPrimary
        )
    }
}

enum class PageTurnDirection { Left, Right }

// ===== TOP BAR =====

@Composable
fun ReaderTopBar(
    title: String,
    progress: Float,
    onBack: () -> Unit,
    onChapterList: () -> Unit,
    onSettings: () -> Unit,
    themeMode: ReaderTheme,
    onThemeChange: (ReaderTheme) -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    val visible by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(300)
    )

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = spacing.ScreenPadding, horizontal = spacing.ScreenPadding)
            .graphicsLayer { alpha = visible },
        variant = com.komorebi.reader.ui.components.GlassComponents.GlassVariant.Strong,
        shape = shapes.pill
    ) {
        Row(
            modifier = Modifier.padding(spacing.Medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(painterResource(android.R.drawable.ic_menu_close_clear_cancel), contentDescription = "Quitter")
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = typography.titleMedium.copy(color = colors.contentPrimary),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
                )
                LiquidProgress(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().padding(top = spacing.XS),
                    height = 2.dp
                )
            }

            Row {
                IconButton(onClick = onChapterList) {
                    Icon(painterResource(android.R.drawable.ic_menu_agenda), contentDescription = "Chapitres")
                }
                IconButton(onClick = onSettings) {
                    Icon(painterResource(android.R.drawable.ic_menu_preferences), contentDescription = "Réglages")
                }
            }
        }
    }
}

// ===== BOTTOM BAR =====

@Composable
fun ReaderBottomBar(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    onPreviousChapter: () -> Unit,
    onNextChapter: () -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = spacing.ScreenPadding + 20.dp, horizontal = spacing.ScreenPadding),
        variant = com.komorebi.reader.ui.components.GlassComponents.GlassVariant.Strong,
        shape = shapes.pill
    ) {
        Column(modifier = Modifier.padding(spacing.Medium)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPreviousChapter) {
                    Icon(painterResource(android.R.drawable.ic_media_previous), contentDescription = "Chapitre précédent")
                }

                Text(
                    text = "${currentPage + 1} / $totalPages",
                    style = typography.readingOverlay.copy(color = colors.contentSecondary)
                )

                IconButton(onClick = onNextChapter) {
                    Icon(painterResource(android.R.drawable.ic_media_next), contentDescription = "Chapitre suivant")
                }
            }

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.Small))

            // Page slider
            androidx.compose.material3.Slider(
                value = (currentPage + 1).toFloat(),
                onValueChange = { onPageChange(it.toInt() - 1) },
                valueRange = 1f..totalPages.toFloat(),
                steps = totalPages - 1,
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.SliderDefaults.colors(
                    thumbColor = colors.primaryGlow,
                    trackColor = colors.surfaceGlassSubtle,
                    activeTrackColor = colors.primary,
                    inactiveTrackColor = colors.surfaceGlassSubtle
                )
            )
        }
    }
}

// ===== CHAPTER LIST SHEET =====

@Composable
fun ChapterListSheet(
    chapters: List<ChapterItem>,
    currentChapter: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    GlassSurface(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.surfaceGlassShadow.copy(alpha = 0.5f))
            .pointerInput(Unit) {
                detectTapGestures(onPress = {}, onRelease = { onDismiss() }, onCancel = {})
            },
        variant = com.komorebi.reader.ui.components.GlassComponents.GlassVariant.Strong,
        shape = shapes.bottomSheet
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Handle
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(4.dp)
                    .padding(top = spacing.Medium)
                    .background(colors.borderGlass, shapes.pill)
            )

            Text(
                text = "Chapitres",
                style = typography.headlineSmall.copy(color = colors.contentPrimary),
                modifier = Modifier.padding(top = spacing.Medium)
            )

            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(spacing.XS),
                contentPadding = spacing.ScreenPadding
            ) {
                items(chapters) { chapter ->
                    val isCurrent = chapter.id == currentChapter
                    GlassCard(
                        variant = if (isCurrent) com.komorebi.reader.ui.components.GlassComponents.GlassCardVariant.Glow else com.komorebi.reader.ui.components.GlassComponents.GlassCardVariant.Standard,
                        onClick = { onSelect(chapter.id) }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(spacing.Medium),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Chapitre ${chapter.number.toInt()}",
                                    style = typography.titleSmall.copy(color = colors.contentPrimary)
                                )
                                Text(
                                    text = chapter.title,
                                    style = typography.bodySmall.copy(color = colors.contentTertiary),
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
                                )
                            }
                            if (chapter.read) {
                                Icon(painterResource(android.R.drawable.checkbox_on_background), contentDescription = "Lu", tint = colors.statusSuccess)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ===== READER SETTINGS SHEET =====

@Composable
fun ReaderSettingsSheet(
    readingDirection: ReadingDirection,
    onDirectionChange: (ReadingDirection) -> Unit,
    themeMode: ReaderTheme,
    onThemeChange: (ReaderTheme) -> Unit,
    zoomLevel: Float,
    onZoomChange: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    GlassSurface(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.surfaceGlassShadow.copy(alpha = 0.5f))
            .pointerInput(Unit) {
                detectTapGestures(onPress = {}, onRelease = { onDismiss() }, onCancel = {})
            },
        variant = com.komorebi.reader.ui.components.GlassComponents.GlassVariant.Strong,
        shape = shapes.bottomSheet
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(4.dp)
                    .padding(top = spacing.Medium)
                    .background(colors.borderGlass, shapes.pill)
            )

            Text(
                text = "Réglages de lecture",
                style = typography.headlineSmall.copy(color = colors.contentPrimary),
                modifier = Modifier.padding(top = spacing.Medium)
            )

            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(spacing.Medium),
                contentPadding = spacing.ScreenPadding
            ) {
                item {
                    SettingRow(
                        label = "Sens de lecture",
                        value = readingDirection.label,
                        onClick = { /* Show direction picker */ }
                    )
                }
                item {
                    SettingRow(
                        label = "Thème",
                        value = themeMode.label,
                        onClick = { /* Show theme picker */ }
                    )
                }
                item {
                    SettingRow(
                        label = "Zoom",
                        value = "${(zoomLevel * 100).toInt()}%",
                        onClick = { /* Show zoom slider */ }
                    )
                }
                item {
                    SettingRow(
                        label = "Police",
                        value = "Système",
                        onClick = { /* Show font picker */ }
                    )
                }
                item {
                    SettingRow(
                        label = "Taille police",
                        value = "Moyen",
                        onClick = { /* Show size picker */ }
                    )
                }
                item {
                    SettingRow(
                        label = "Interligne",
                        value = "Normal",
                        onClick = { /* Show line height picker */ }
                    )
                }
                item {
                    SettingRow(
                        label = "Marges",
                        value = "Normales",
                        onClick = { /* Show margin picker */ }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        variant = com.komorebi.reader.ui.components.GlassComponents.GlassCardVariant.Standard,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(spacing.Medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, style = typography.bodyMedium.copy(color = colors.contentPrimary))
            Text(text = value, style = typography.bodyMedium.copy(color = colors.contentTertiary))
            Icon(painterResource(android.R.drawable.ic_media_play), contentDescription = "Modifier", tint = colors.contentTertiary)
        }
    }
}

// ===== DATA MODELS =====

enum class ReadingDirection(val label: String) {
    Vertical("Vertical (Webtoon)"),
    RightToLeft("Droite → Gauche (Manga)"),
    LeftToRight("Gauche → Droite (Comics)"),
    VerticalRtl("Vertical RTL")
}

enum class ReaderTheme(val label: String) {
    Dark("Sombre"),
    Light("Clair"),
    Sepia("Sépia")
}

enum class SwipeDirection { Left, Right, Up, Down }

data class PageImage(
    val url: String,
    val width: Int,
    val height: Int,
    val index: Int
)

data class ChapterItem(
    val id: String,
    val title: String,
    val number: Double,
    val pages: Int,
    val read: Boolean
)

data class ChapterDetail(
    val id: String,
    val title: String,
    val number: Double,
    val pages: List<PageImage>
)

// ===== SAMPLE DATA =====

fun getSampleChapter(id: String): ChapterDetail {
    return ChapterDetail(
        id = id,
        title = "Chapitre 1: Le début de l'aventure",
        number = 1.0,
        pages = (1..20).map { PageImage("page$it.jpg", 800, 1200, it) }
    )
}

fun getSampleChapters(): List<ChapterItem> = (1..50).map { i ->
    ChapterItem(
        id = "ch$i",
        title = "Chapitre $i",
        number = i.toDouble(),
        pages = 19,
        read = i <= 10
    )
}