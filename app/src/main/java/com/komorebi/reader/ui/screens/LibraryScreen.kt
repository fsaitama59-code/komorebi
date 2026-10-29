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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.komorebi.reader.ui.navigation.NavBarItem
import com.komorebi.reader.ui.theme.KomorebiMotion
import com.komorebi.reader.ui.theme.KomorebiSpacing
import com.komorebi.reader.ui.theme.KomorebiTypeScale
import com.komorebi.reader.ui.theme.komorebiColors
import com.komorebi.reader.ui.theme.komorebiShapes
import com.komorebi.reader.ui.theme.komorebiTypography
import kotlinx.coroutines.launch

/**
 * Library Screen - Premium glass/liquid library view
 * Grid/List toggle, collections, reading progress, smart filters
 */

@Composable
fun LibraryScreen(
    onOpenReader: (String, String) -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    var viewMode by remember { mutableStateOf(ViewMode.Grid) }
    var selectedFilter by remember { mutableStateOf(LibraryFilter.All) }
    var searchQuery by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }

    // Sample data - in real app from ViewModel
    val books = remember { generateSampleBooks() }
    val filteredBooks = books.filter { book ->
        (selectedFilter == LibraryFilter.All || book.type == selectedFilter) &&
        book.title.lowercase().contains(searchQuery.lowercase())
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header with search and filters
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
                            text = "Bibliothèque",
                            style = typography.headlineMedium.copy(color = colors.contentPrimary)
                        )
                        Text(
                            text = "${filteredBooks.size} ouvrages",
                            style = typography.bodySmall.copy(color = colors.contentTertiary)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // View mode toggle
                        Row(
                            modifier = Modifier.padding(end = spacing.Small)
                        ) {
                            ViewMode.values().forEach { mode ->
                                LiquidChip(
                                    label = "",
                                    selected = viewMode == mode,
                                    onClick = { viewMode = mode },
                                    leadingIcon = { Icon(painterResource(mode.iconRes), contentDescription = mode.label) },
                                    variant = com.komorebi.reader.ui.components.GlassComponents.LiquidChipVariant.Outline
                                )
                            }
                        }

                        // Filter chip
                        LiquidChip(
                            label = selectedFilter.label,
                            selected = selectedFilter != LibraryFilter.All,
                            onClick = { showFilterSheet = true },
                            leadingIcon = { Icon(painterResource(android.R.drawable.ic_menu_sort_by_size), contentDescription = "Filtrer") },
                            variant = com.komorebi.reader.ui.components.GlassComponents.LiquidChipVariant.Standard
                        )
                    }
                }

                // Search bar
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.Medium))
                GlassSurface(
                    modifier = Modifier.fillMaxWidth(),
                    variant = com.komorebi.reader.ui.components.GlassComponents.GlassVariant.Standard,
                    shape = shapes.pill
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = spacing.Medium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painterResource(android.R.drawable.ic_menu_search), contentDescription = "Rechercher", tint = colors.contentTertiary)
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(spacing.Small))
                        androidx.compose.material3.TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Rechercher...", color = colors.contentTertiary, style = typography.bodyMedium) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = spacing.Small),
                            colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                textColor = colors.contentPrimary,
                                placeholderColor = colors.contentTertiary,
                                cursorColor = colors.primaryGlow
                            ),
                            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions.Default.copy(
                                imeAction = androidx.compose.ui.text.input.ImeAction.Search
                            )
                        )
                    }
                }
            }
        }

        // Content area
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.Medium))

        if (viewMode == ViewMode.Grid) {
            GridView(books = filteredBooks, onOpenReader = onOpenReader)
        } else {
            ListView(books = filteredBooks, onOpenReader = onOpenReader)
        }

        // Floating action button
        LiquidFAB(
            onClick = { /* Open add book / import */ },
            modifier = Modifier
                .padding(spacing.ScreenPadding)
                .align(Alignment.BottomEnd),
            icon = { Icon(painterResource(android.R.drawable.ic_menu_add), contentDescription = "Ajouter") }
        )
    }
}

// ===== GRID VIEW =====

@Composable
fun GridView(
    books: List<BookItem>,
    onOpenReader: (String, String) -> Unit
) {
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacing.ScreenPadding, vertical = spacing.Medium),
        cells = GridCells.Adaptive(minSize = 160.dp),
        verticalArrangement = Arrangement.spacedBy(spacing.Medium),
        horizontalArrangement = Arrangement.spacedBy(spacing.Medium),
        contentPadding = spacing.ScreenPadding
    ) {
        items(books) { book ->
            BookCoverCard(
                book = book,
                onClick = { onOpenReader(book.id, book.chapters.firstOrNull()?.id ?: "") }
            )
        }
    }
}

@Composable
fun BookCoverCard(
    book: BookItem,
    onClick: () -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f),
        variant = com.komorebi.reader.ui.components.GlassComponents.GlassCardVariant.Elevated,
        onClick = onClick
    ) {
        // Cover image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .background(colors.surfaceGlassStrong, shapes.coverImage),
            contentAlignment = Alignment.TopEnd
        ) {
            // Progress indicator overlay
            if (book.progress > 0f) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.Small),
                    contentAlignment = Alignment.TopEnd
                ) {
                    LiquidProgress(
                        progress = book.progress,
                        modifier = Modifier.width(48.dp),
                        variant = com.komorebi.reader.ui.components.GlassComponents.LiquidProgressVariant.Circular
                    )
                }
            }

            // Book type badge
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .padding(spacing.Small)
                    .align(Alignment.TopStart)
            ) {
                GlassSurface(
                    variant = com.komorebi.reader.ui.components.GlassComponents.GlassVariant.Strong,
                    shape = shapes.chip
                ) {
                    Text(
                        text = book.type.shortLabel,
                        style = typography.labelSmall.copy(color = colors.contentPrimary),
                        modifier = Modifier.padding(horizontal = spacing.Small, vertical = spacing.XS)
                    )
                }
            }
        }

        // Book info
        Column(modifier = Modifier.padding(top = spacing.Small)) {
            Text(
                text = book.title,
                style = typography.titleMedium.copy(color = colors.contentPrimary),
                maxLines = 2,
                overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.XS))
            Text(
                text = book.author,
                style = typography.bodySmall.copy(color = colors.contentTertiary),
                maxLines = 1,
                overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
            )

            // Progress bar
            if (book.progress > 0f && book.progress < 1f) {
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.Small))
                LiquidProgress(
                    progress = book.progress,
                    modifier = Modifier.fillMaxWidth(),
                    height = 3.dp
                )
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.XS))
                Text(
                    text = "${(book.progress * 100).toInt()}% lu",
                    style = typography.labelSmall.copy(color = colors.contentTertiary)
                )
            }
        }
    }
}

// ===== LIST VIEW =====

@Composable
fun ListView(
    books: List<BookItem>,
    onOpenReader: (String, String) -> Unit
) {
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacing.ScreenPadding, vertical = spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(spacing.Small),
        contentPadding = spacing.ScreenPadding
    ) {
        items(books) { book ->
            BookListItem(
                book = book,
                onClick = { onOpenReader(book.id, book.chapters.firstOrNull()?.id ?: "") }
            )
        }
    }
}

@Composable
fun BookListItem(
    book: BookItem,
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cover thumbnail
            Box(
                modifier = Modifier
                    .size(60.dp, 85.dp)
                    .background(colors.surfaceGlassStrong, shapes.thumbnail)
            ) {
                if (book.progress > 0f) {
                    LiquidProgress(
                        progress = book.progress,
                        variant = com.komorebi.reader.ui.components.GlassComponents.LiquidProgressVariant.Circular,
                        modifier = Modifier.size(32.dp).align(Alignment.TopEnd).padding(spacing.XS)
                    )
                }
            }

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(spacing.Medium))

            // Info
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = book.type.shortLabel,
                        style = typography.labelSmall.copy(color = colors.primaryGlow)
                    )
                    Text(
                        text = book.lastRead?.let { "Lu il y a ${it}" } ?: "Non lu",
                        style = typography.labelSmall.copy(color = colors.contentTertiary)
                    )
                }

                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.XS))

                Text(
                    text = book.title,
                    style = typography.titleMedium.copy(color = colors.contentPrimary),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
                )

                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.XS))

                Text(
                    text = book.author,
                    style = typography.bodySmall.copy(color = colors.contentSecondary),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
                )

                if (book.progress > 0f && book.progress < 1f) {
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.Small))
                    LiquidProgress(
                        progress = book.progress,
                        modifier = Modifier.fillMaxWidth(),
                        height = 2.dp
                    )
                }
            }

            // Menu button
            androidx.compose.material3.IconButton(onClick = { /* Show menu */ }) {
                Icon(painterResource(android.R.drawable.ic_menu_more), contentDescription = "Plus d'options")
            }
        }
    }
}

// ===== DATA MODELS =====

enum class ViewMode {
    Grid(android.R.drawable.ic_menu_view),
    List(android.R.drawable.ic_menu_sort_by_size);

    val iconRes: Int
    constructor(iconRes: Int) { this.iconRes = iconRes }
}

enum class LibraryFilter(val label: String, val shortLabel: String) {
    All("Tout", "TOUT"),
    Manga("Mangas", "MANGA"),
    Novel("Romans", "ROMAN"),
    Comic("Comics", "COMIC"),
    Magazine("Magazines", "MAG");

    constructor(label: String, shortLabel: String) {
        this.label = label
        this.shortLabel = shortLabel
    }
}

data class BookItem(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val type: LibraryFilter,
    val progress: Float,
    val chapters: List<ChapterItem>,
    val lastRead: String?,
    val tags: List<String>
)

data class ChapterItem(
    val id: String,
    val title: String,
    val number: Double,
    val pages: Int,
    val read: Boolean
)

// ===== SAMPLE DATA =====

fun generateSampleBooks(): List<BookItem> = listOf(
    BookItem(
        id = "1",
        title = "One Piece",
        author = "Eiichiro Oda",
        coverUrl = null,
        type = LibraryFilter.Manga,
        progress = 0.65f,
        chapters = (1..100).map { ChapterItem("ch$it", "Chapitre $it", it.toDouble(), 19, it <= 65) },
        lastRead = "2 jours",
        tags = listOf("Shonen", "Aventure", "Pirates", "Long-running")
    ),
    BookItem(
        id = "2",
        title = "Attack on Titan",
        author = "Hajime Isayama",
        coverUrl = null,
        type = LibraryFilter.Manga,
        progress = 1f,
        chapters = (1..139).map { ChapterItem("ch$it", "Chapitre $it", it.toDouble(), 18, true) },
        lastRead = "1 mois",
        tags = listOf("Seinen", "Action", "Mystère", "Terminé")
    ),
    BookItem(
        id = "3",
        title = "Jujutsu Kaisen",
        author = "Gege Akutami",
        coverUrl = null,
        type = LibraryFilter.Manga,
        progress = 0.32f,
        chapters = (1..250).map { ChapterItem("ch$it", "Chapitre $it", it.toDouble(), 19, it <= 80) },
        lastRead = "1 semaine",
        tags = listOf("Shonen", "Surnaturel", "Action", "En cours")
    ),
    BookItem(
        id = "4",
        title = "The Name of the Wind",
        author = "Patrick Rothfuss",
        coverUrl = null,
        type = LibraryFilter.Novel,
        progress = 0.45f,
        chapters = (1..50).map { ChapterItem("ch$it", "Chapitre $it", it.toDouble(), 30, it <= 22) },
        lastRead = "3 jours",
        tags = listOf("Fantasy", "Épique", "Magie")
    ),
    BookItem(
        id = "5",
        title = "Berserk Deluxe",
        author = "Kentaro Miura",
        coverUrl = null,
        type = LibraryFilter.Manga,
        progress = 0.18f,
        chapters = (1..40).map { ChapterItem("ch$it", "Volume $it", it.toDouble(), 200, it <= 7) },
        lastRead = "2 semaines",
        tags = listOf("Seinen", "Dark Fantasy", "Chef-d'œuvre", "Inachevé")
    ),
    BookItem(
        id = "6",
        title = "Vinland Saga",
        author = "Makoto Yukimura",
        coverUrl = null,
        type = LibraryFilter.Manga,
        progress = 0.72f,
        chapters = (1..200).map { ChapterItem("ch$it", "Chapitre $it", it.toDouble(), 20, it <= 144) },
        lastRead = "5 jours",
        tags = listOf("Seinen", "Historique", "Vikings", "Philosophique")
    )
)

// ===== ENUMS FOR LIBRARY =====

enum class ViewMode {
    Grid(android.R.drawable.ic_menu_view),
    List(android.R.drawable.ic_menu_sort_by_size);

    val iconRes: Int
    constructor(iconRes: Int) { this.iconRes = iconRes }
}

enum class LibraryFilter(val label: String, val shortLabel: String) {
    All("Tout", "TOUT"),
    Manga("Mangas", "MANGA"),
    Novel("Romans", "ROMAN"),
    Comic("Comics", "COMIC"),
    Magazine("Magazines", "MAG");

    constructor(label: String, shortLabel: String) {
        this.label = label
        this.shortLabel = shortLabel
    }
}