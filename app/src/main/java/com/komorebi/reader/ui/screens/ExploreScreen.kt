package com.komorebi.reader.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komorebi.reader.ui.components.GlassComponents.GlassCard
import com.komorebi.reader.ui.components.GlassComponents.GlassSurface
import com.komorebi.reader.ui.components.GlassComponents.LiquidButton
import com.komorebi.reader.ui.components.GlassComponents.LiquidChip
import com.komorebi.reader.ui.components.GlassComponents.LiquidFAB
import com.komorebi.reader.ui.components.GlassComponents.LiquidProgress
import com.komorebi.reader.ui.theme.KomorebiMotion
import com.komorebi.reader.ui.theme.KomorebiSpacing
import com.komorebi.reader.ui.theme.komorebiColors
import com.komorebi.reader.ui.theme.komorebiShapes
import com.komorebi.reader.ui.theme.komorebiTypography
import kotlinx.coroutines.launch

/**
 * Explore Screen - Discover new manga/books from online sources
 * Premium glass UI with source selection, trending, recommendations
 */

@Composable
fun ExploreScreen(
    onOpenReader: (String, String) -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    var selectedSource by remember { mutableStateOf(ExploreSource.MangaDex) }
    var selectedCategory by remember { mutableStateOf(ExploreCategory.Trending) }
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        GlassSurface(
            modifier = Modifier.fillMaxWidth(),
            variant = com.komorebi.reader.ui.components.GlassComponents.GlassVariant.Subtle
        ) {
            Column(modifier = Modifier.padding(spacing.ScreenPadding)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Explorer",
                            style = typography.headlineMedium.copy(color = colors.contentPrimary)
                        )
                        Text(
                            text = "Découvrir de nouvelles séries",
                            style = typography.bodySmall.copy(color = colors.contentTertiary)
                        )
                    }
                }

                // Source selector
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.Medium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.Small)
                ) {
                    ExploreSource.values().forEach { source ->
                        LiquidChip(
                            label = source.label,
                            selected = selectedSource == source,
                            onClick = { selectedSource = source },
                            variant = com.komorebi.reader.ui.components.GlassComponents.LiquidChipVariant.Standard
                        )
                    }
                }

                // Category tabs
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.Medium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.Small)
                ) {
                    ExploreCategory.values().forEach { category ->
                        LiquidChip(
                            label = category.label,
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            variant = com.komorebi.reader.ui.components.GlassComponents.LiquidChipVariant.Outline
                        )
                    }
                }
            }
        }

        // Content
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(spacing.XL),
            contentPadding = spacing.ScreenPadding
        ) {
            // Trending section
            item {
                SectionHeader(
                    title = "Tendances",
                    actionLabel = "Voir tout",
                    onActionClick = { /* Navigate to full list */ }
                )
            }
            item {
                HorizontalCarousel(
                    items = getTrendingItems(),
                    onItemClick = { item -> onOpenReader(item.id, item.chapters.firstOrNull()?.id ?: "") }
                )
            }

            // New releases
            item {
                SectionHeader(
                    title = "Nouveautés",
                    actionLabel = "Voir tout",
                    onActionClick = { /* Navigate */ }
                )
            }
            item {
                HorizontalCarousel(
                    items = getNewReleases(),
                    onItemClick = { item -> onOpenReader(item.id, item.chapters.firstOrNull()?.id ?: "") }
                )
            }

            // Recommendations
            item {
                SectionHeader(
                    title = "Recommandés pour vous",
                    actionLabel = "Voir tout",
                    onActionClick = { /* Navigate */ }
                )
            }
            item {
                HorizontalCarousel(
                    items = getRecommendations(),
                    onItemClick = { item -> onOpenReader(item.id, item.chapters.firstOrNull()?.id ?: "") }
                )
            }

            // Genres
            item {
                SectionHeader(
                    title = "Genres populaires",
                    actionLabel = "Tous les genres",
                    onActionClick = { /* Navigate */ }
                )
            }
            item {
                GenreGrid(onGenreClick = { genre -> /* Filter by genre */ })
            }

            // Sources
            item {
                SectionHeader(
                    title = "Sources disponibles",
                    actionLabel = "Gérer",
                    onActionClick = { /* Navigate to source settings */ }
                )
            }
            item {
                SourceGrid(onSourceClick = { selectedSource = it })
            }
        }
    }
}

// ===== SECTION HEADER =====

@Composable
fun SectionHeader(
    title: String,
    actionLabel: String,
    onActionClick: () -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = typography.headlineSmall.copy(color = colors.contentPrimary))
        androidx.compose.material3.TextButton(
            onClick = onActionClick,
            modifier = Modifier.padding(end = spacing.Small)
        ) {
            Text(text = actionLabel, style = typography.labelMedium.copy(color = colors.primaryGlow))
        }
    }
}

// ===== HORIZONTAL CAROUSEL =====

@Composable
fun HorizontalCarousel(
    items: List<ExploreItem>,
    onItemClick: (ExploreItem) -> Unit
) {
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    androidx.compose.foundation.lazy.LazyRow(
        modifier = Modifier.fillMaxWidth().height(280.dp),
        horizontalArrangement = Arrangement.spacedBy(spacing.Medium),
        contentPadding = spacing.ScreenPadding
    ) {
        items(items) { item ->
            ExploreCard(item = item, onClick = { onItemClick(item) })
        }
    }
}

@Composable
fun ExploreCard(
    item: ExploreItem,
    onClick: () -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    GlassCard(
        modifier = Modifier
            .width(140.dp)
            .height(280.dp),
        variant = com.komorebi.reader.ui.components.GlassComponents.GlassCardVariant.Elevated,
        onClick = onClick
    ) {
        Column {
            // Cover
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
                    .background(colors.surfaceGlassStrong, shapes.coverImage)
            ) {
                // Source badge
                Box(
                    modifier = Modifier
                        .padding(spacing.Small)
                        .align(Alignment.TopStart)
                ) {
                    GlassSurface(
                        variant = com.komorebi.reader.ui.components.GlassComponents.GlassVariant.Strong,
                        shape = shapes.chip
                    ) {
                        Text(
                            text = item.source.shortLabel,
                            style = typography.labelSmall.copy(color = colors.contentPrimary),
                            modifier = Modifier.padding(horizontal = spacing.Small, vertical = spacing.XS)
                        )
                    }
                }
            }

            // Info
            Column(modifier = Modifier.padding(spacing.Small).fillMaxWidth()) {
                Text(
                    text = item.title,
                    style = typography.titleSmall.copy(color = colors.contentPrimary),
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
                )
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.XS))
                Text(
                    text = item.author,
                    style = typography.bodySmall.copy(color = colors.contentTertiary),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
                )
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.XS))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.Small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painterResource(android.R.drawable.btn_star_big_on), contentDescription = "Note", tint = colors.secondaryGlow, modifier = Modifier.size(14.dp))
                    Text(text = item.rating.toString(), style = typography.labelSmall.copy(color = colors.contentSecondary))
                    Text(text = "• ${item.chapterCount} ch.", style = typography.labelSmall.copy(color = colors.contentTertiary))
                }
            }
        }
    }
}

// ===== GENRE GRID =====

@Composable
fun GenreGrid(onGenreClick: (String) -> Unit) {
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        cells = GridCells.Adaptive(minSize = 100.dp),
        verticalArrangement = Arrangement.spacedBy(spacing.Small),
        horizontalArrangement = Arrangement.spacedBy(spacing.Small),
        contentPadding = spacing.ScreenPadding
    ) {
        items(getGenres()) { genre ->
            GenreChip(genre = genre, onClick = { onGenreClick(genre) })
        }
    }
}

@Composable
fun GenreChip(
    genre: String,
    onClick: () -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {}, onRelease = { onClick() }, onCancel = {})
            }
            .combinedClickable(onClick = onClick, onLongClick = {}),
        variant = com.komorebi.reader.ui.components.GlassComponents.GlassVariant.Standard,
        shape = shapes.glassCard
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = genre,
                style = typography.titleMedium.copy(color = colors.contentPrimary),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ===== SOURCE GRID =====

@Composable
fun SourceGrid(onSourceClick: (ExploreSource) -> Unit) {
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        cells = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(spacing.Small),
        horizontalArrangement = Arrangement.spacedBy(spacing.Small),
        contentPadding = spacing.ScreenPadding
    ) {
        items(ExploreSource.values()) { source ->
            SourceCard(source = source, onClick = { onSourceClick(source) })
        }
    }
}

@Composable
fun SourceCard(
    source: ExploreSource,
    onClick: () -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {}, onRelease = { onClick() }, onCancel = {})
            }
            .combinedClickable(onClick = onClick, onLongClick = {}),
        variant = com.komorebi.reader.ui.components.GlassComponents.GlassVariant.Standard,
        shape = shapes.glassCard
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(spacing.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Source icon/logo
            Box(
                modifier = Modifier.size(32.dp)
                    .background(colors.primary.copy(alpha = 0.2f), shapes.medium)
            ) {
                Icon(
                    painter = painterResource(android.R.drawable.ic_menu_gallery),
                    contentDescription = source.label,
                    tint = colors.primaryGlow
                )
            }
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.Small))
            Text(
                text = source.label,
                style = typography.labelMedium.copy(color = colors.contentPrimary),
                textAlign = TextAlign.Center
            )
            Text(
                text = source.description,
                style = typography.labelSmall.copy(color = colors.contentTertiary),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

// ===== DATA MODELS =====

enum class ExploreSource(val label: String, val shortLabel: String, val description: String) {
    MangaDex("MangaDex", "MD", "Communautaire, open source"),
    MangaPlus("MANGA Plus", "MP", "Officiel Shueisha"),
    Webtoon("WEBTOON", "WT", "Vertical scrolling"),
    Tachiyomi("Tachiyomi", "TY", "Extension-based"),
    Komga("Komga", "KG", "Auto-hébergé"),
    Kavita("Kavita", "KV", "Serveur personnel")
}

enum class ExploreCategory(val label: String) {
    Trending("Tendances"),
    NewReleases("Nouveautés"),
    Popular("Populaires"),
    Completed("Terminés"),
    Updated("Récemment mis à jour"),
    HighlyRated("Mieux notés")
}

data class ExploreItem(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val source: ExploreSource,
    val rating: Float,
    val chapterCount: Int,
    val tags: List<String>,
    val chapters: List<ChapterItem>
)

data class ChapterItem(
    val id: String,
    val title: String,
    val number: Double
)

// ===== SAMPLE DATA =====

fun getTrendingItems(): List<ExploreItem> = listOf(
    ExploreItem("ex1", "Chainsaw Man", "Tatsuki Fujimoto", null, ExploreSource.MangaDex, 4.8f, 150, listOf("Action", "Horror", "Comedy"), listOf()),
    ExploreItem("ex2", "Spy × Family", "Tatsuya Endo", null, ExploreSource.MangaPlus, 4.9f, 120, listOf("Spy", "Comedy", "Family"), listOf()),
    ExploreItem("ex3", "Kaiju No. 8", "Naoya Matsumoto", null, ExploreSource.MangaDex, 4.7f, 100, listOf("Action", "Sci-Fi", "Kaiju"), listOf()),
    ExploreItem("ex4", "Oshi no Ko", "Aka Akasaka", null, ExploreSource.MangaDex, 4.8f, 140, listOf("Drama", "Mystery", "Supernatural"), listOf()),
    ExploreItem("ex5", "Frieren", "Kanehito Yamada", null, ExploreSource.MangaDex, 4.9f, 120, listOf("Fantasy", "Adventure", "Post-journey"), listOf()),
    ExploreItem("ex6", "Solo Leveling", "Chugong", null, ExploreSource.Webtoon, 4.8f, 200, listOf("Action", "Fantasy", "System"), listOf())
)

fun getNewReleases(): List<ExploreItem> = listOf(
    ExploreItem("nr1", "Sakamoto Days", "Yuto Suzuki", null, ExploreSource.MangaPlus, 4.6f, 150, listOf("Action", "Comedy", "Assassin"), listOf()),
    ExploreItem("nr2", "Kagurabachi", "Takeru Hokazono", null, ExploreSource.MangaPlus, 4.5f, 30, listOf("Action", "Sword", "Revenge"), listOf()),
    ExploreItem("nr3", "Two Faces", "Hiroki Tachibana", null, ExploreSource.MangaDex, 4.3f, 20, listOf("Mystery", "Psychological"), listOf()),
    ExploreItem("nr4", "Cats of the Louvre", "Taiyo Matsumoto", null, ExploreSource.MangaDex, 4.4f, 10, listOf("Art", "Slice of Life", "Cats"), listOf())
)

fun getRecommendations(): List<ExploreItem> = listOf(
    ExploreItem("rec1", "Vinland Saga", "Makoto Yukimura", null, ExploreSource.MangaDex, 4.9f, 200, listOf("Historical", "Vikings", "Philosophical"), listOf()),
    ExploreItem("rec2", "Monster", "Naoki Urasawa", null, ExploreSource.MangaDex, 4.9f, 162, listOf("Psychological", "Thriller", "Masterpiece"), listOf()),
    ExploreItem("rec3", "20th Century Boys", "Naoki Urasawa", null, ExploreSource.MangaDex, 4.8f, 220, listOf("Mystery", "Sci-Fi", "Epic"), listOf()),
    ExploreItem("rec4", "Pluto", "Naoki Urasawa", null, ExploreSource.MangaDex, 4.8f, 65, listOf("Sci-Fi", "Mystery", "Astro Boy"), listOf())
)

fun getGenres(): List<String> = listOf(
    "Action", "Aventure", "Comédie", "Drame", "Fantastique",
    "Horreur", "Mystère", "Romance", "Sci-Fi", "Slice of Life",
    "Sports", "Supernatural", "Thriller", "Isekai", "Mecha",
    "Historical", "Psychological", "Seinen", "Shonen", "Shojo",
    "Josei", "Yaoi", "Yuri", "Gourmet", "Music", "School"
)