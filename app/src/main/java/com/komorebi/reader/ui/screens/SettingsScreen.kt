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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komorebi.reader.ui.components.GlassComponents.GlassCard
import com.komorebi.reader.ui.components.GlassComponents.GlassSurface
import com.komorebi.reader.ui.components.GlassComponents.LiquidButton
import com.komorebi.reader.ui.components.GlassComponents.LiquidChip
import com.komorebi.reader.ui.components.GlassComponents.LiquidSwitch
import com.komorebi.reader.ui.theme.KomorebiSpacing
import com.komorebi.reader.ui.theme.komorebiColors
import com.komorebi.reader.ui.theme.komorebiShapes
import com.komorebi.reader.ui.theme.komorebiTypography

/**
 * Settings Screen - Premium glass settings with liquid toggles
 * Appearance, reading, library, sources, sync, about
 */

@Composable
fun SettingsScreen() {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    // Settings state (in real app from DataStore/ViewModel)
    var themeMode by remember { mutableStateOf(AppThemeMode.System) }
    var autoNightMode by remember { mutableStateOf(true) }
    var readingDirection by remember { mutableStateOf(ReadingDirection.RightToLeft) }
    var readingTheme by remember { mutableStateOf(ReaderTheme.Dark) }
    var fontSize by remember { mutableStateOf(FontSize.Medium) }
    var lineHeight by remember { mutableStateOf(LineHeight.Normal) }
    var margins by remember { mutableStateOf(Margins.Normal) }
    var keepScreenOn by remember { mutableStateOf(true) }
    var autoAdvance by remember { mutableStateOf(false) }
    var swipeToTurn by remember { mutableStateOf(true) }
    var doubleTapZoom by remember { mutableStateOf(true) }
    var showProgress by remember { mutableStateOf(true) }
    var groupBySeries by remember { mutableStateOf(true) }
    var showCovers by remember { mutableStateOf(true) }
    var sortOrder by remember { mutableStateOf(SortOrder.RecentlyRead) }
    var syncEnabled by remember { mutableStateOf(false) }
    var wifiOnlySync by remember { mutableStateOf(true) }
    var autoBackup by remember { mutableStateOf(false) }

    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(spacing.Medium),
        contentPadding = spacing.ScreenPadding
    ) {
        // Appearance section
        SettingsSection(title = "Apparence") {
            SettingCard(
                title = "Thème",
                subtitle = "Choisir le thème de l'application",
                trailing = {
                    LiquidChip(
                        label = themeMode.label,
                        selected = false,
                        onClick = { /* Show theme picker */ },
                        variant = com.komorebi.reader.ui.components.GlassComponents.LiquidChipVariant.Outline
                    )
                }
            )

            SettingCard(
                title = "Mode nuit automatique",
                subtitle = "Basculer automatiquement au coucher du soleil",
                trailing = {
                    LiquidSwitch(
                        checked = autoNightMode,
                        onCheckedChange = { autoNightMode = it }
                    )
                }
            )

            SettingCard(
                title = "Couleur d'accent",
                subtitle = "Personnaliser la couleur principale",
                trailing = {
                    // Color picker preview
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(colors.primaryGlow, shapes.medium)
                    )
                }
            )
        }

        // Reading section
        SettingsSection(title = "Lecture") {
            SettingCard(
                title = "Sens de lecture par défaut",
                subtitle = "Manga (droite→gauche) / Webtoon (vertical) / Comics (gauche→droite)",
                trailing = {
                    LiquidChip(
                        label = readingDirection.label,
                        selected = false,
                        onClick = { /* Show direction picker */ },
                        variant = com.komorebi.reader.ui.components.GlassComponents.LiquidChipVariant.Outline
                    )
                }
            )

            SettingCard(
                title = "Thème de lecture",
                subtitle = "Sombre / Clair / Sépia",
                trailing = {
                    LiquidChip(
                        label = readingTheme.label,
                        selected = false,
                        onClick = { /* Show theme picker */ },
                        variant = com.komorebi.reader.ui.components.GlassComponents.LiquidChipVariant.Outline
                    )
                }
            )

            SettingCard(
                title = "Taille de police",
                subtitle = "Ajuster la taille du texte",
                trailing = {
                    LiquidChip(
                        label = fontSize.label,
                        selected = false,
                        onClick = { /* Show size picker */ },
                        variant = com.komorebi.reader.ui.components.GlassComponents.LiquidChipVariant.Outline
                    )
                }
            )

            SettingCard(
                title = "Interligne",
                subtitle = "Espacement entre les lignes",
                trailing = {
                    LiquidChip(
                        label = lineHeight.label,
                        selected = false,
                        onClick = { /* Show line height picker */ },
                        variant = com.komorebi.reader.ui.components.GlassComponents.LiquidChipVariant.Outline
                    )
                }
            )

            SettingCard(
                title = "Marges",
                subtitle = "Espacement des bords de page",
                trailing = {
                    LiquidChip(
                        label = margins.label,
                        selected = false,
                        onClick = { /* Show margins picker */ },
                        variant = com.komorebi.reader.ui.components.GlassComponents.LiquidChipVariant.Outline
                    )
                }
            )
        }

        // Reading behavior
        SettingsSection(title = "Comportement de lecture") {
            SettingCard(
                title = "Garder l'écran allumé",
                subtitle = "Empêcher la mise en veille pendant la lecture",
                trailing = {
                    LiquidSwitch(
                        checked = keepScreenOn,
                        onCheckedChange = { keepScreenOn = it }
                    )
                }
            )

            SettingCard(
                title = "Avance automatique",
                subtitle = "Passer au chapitre suivant automatiquement",
                trailing = {
                    LiquidSwitch(
                        checked = autoAdvance,
                        onCheckedChange = { autoAdvance = it }
                    )
                }
            )

            SettingCard(
                title = "Balayer pour tourner",
                subtitle = "Geste de balayage pour changer de page",
                trailing = {
                    LiquidSwitch(
                        checked = swipeToTurn,
                        onCheckedChange = { swipeToTurn = it }
                    )
                }
            )

            SettingCard(
                title = "Double tap pour zoomer",
                subtitle = "Zoom rapide sur les pages",
                trailing = {
                    LiquidSwitch(
                        checked = doubleTapZoom,
                        onCheckedChange = { doubleTapZoom = it }
                    )
                }
            )

            SettingCard(
                title = "Afficher la progression",
                subtitle = "Barre de progression en bas de l'écran",
                trailing = {
                    LiquidSwitch(
                        checked = showProgress,
                        onCheckedChange = { showProgress = it }
                    )
                }
            )
        }

        // Library section
        SettingsSection(title = "Bibliothèque") {
            SettingCard(
                title = "Grouper par série",
                subtitle = "Regrouper les chapitres par série",
                trailing = {
                    LiquidSwitch(
                        checked = groupBySeries,
                        onCheckedChange = { groupBySeries = it }
                    )
                }
            )

            SettingCard(
                title = "Afficher les couvertures",
                subtitle = "Vignettes dans la liste",
                trailing = {
                    LiquidSwitch(
                        checked = showCovers,
                        onCheckedChange = { showCovers = it }
                    )
                }
            )

            SettingCard(
                title = "Tri par défaut",
                subtitle = "Ordre d'affichage des livres",
                trailing = {
                    LiquidChip(
                        label = sortOrder.label,
                        selected = false,
                        onClick = { /* Show sort picker */ },
                        variant = com.komorebi.reader.ui.components.GlassComponents.LiquidChipVariant.Outline
                    )
                }
            )

            SettingCard(
                title = "Importer des fichiers",
                subtitle = "Ajouter CBZ, PDF, EPUB depuis le stockage",
                trailing = {
                    Icon(painterResource(android.R.drawable.ic_menu_upload), contentDescription = "Importer", tint = colors.contentTertiary)
                },
                onClick = { /* Open file picker */ }
            )

            SettingCard(
                title = "Scanner la bibliothèque",
                subtitle = "Détecter automatiquement les nouveaux fichiers",
                trailing = {
                    Icon(painterResource(android.R.drawable.ic_menu_search), contentDescription = "Scanner", tint = colors.contentTertiary)
                },
                onClick = { /* Trigger scan */ }
            )
        }

        // Sources section
        SettingsSection(title = "Sources en ligne") {
            SettingCard(
                title = "Gérer les extensions",
                subtitle = "Installer, mettre à jour, configurer les sources",
                trailing = {
                    Icon(painterResource(android.R.drawable.ic_menu_manage), contentDescription = "Gérer", tint = colors.contentTertiary)
                },
                onClick = { /* Navigate to extensions */ }
            )

            SettingCard(
                title = "Ordre de priorité",
                subtitle = "Définir l'ordre de recherche des sources",
                trailing = {
                    Icon(painterResource(android.R.drawable.ic_menu_sort_by_size), contentDescription = "Ordre", tint = colors.contentTertiary)
                },
                onClick = { /* Navigate to priority */ }
            )
        }

        // Sync section
        SettingsSection(title = "Synchronisation") {
            SettingCard(
                title = "Activer la synchronisation",
                subtitle = "Synchroniser progression, favoris, bibliothèque",
                trailing = {
                    LiquidSwitch(
                        checked = syncEnabled,
                        onCheckedChange = { syncEnabled = it }
                    )
                }
            )

            SettingCard(
                title = "Wi-Fi uniquement",
                subtitle = "Ne synchroniser que sur Wi-Fi",
                trailing = {
                    LiquidSwitch(
                        checked = wifiOnlySync,
                        onCheckedChange = { wifiOnlySync = it },
                        enabled = syncEnabled
                    )
                },
                enabled = syncEnabled
            )

            SettingCard(
                title = "Sauvegarde automatique",
                subtitle = "Sauvegarder la bibliothèque dans le cloud",
                trailing = {
                    LiquidSwitch(
                        checked = autoBackup,
                        onCheckedChange = { autoBackup = it },
                        enabled = syncEnabled
                    )
                },
                enabled = syncEnabled
            )

            SettingCard(
                title = "Synchroniser maintenant",
                subtitle = "Forcer une synchronisation immédiate",
                trailing = {
                    Icon(painterResource(android.R.drawable.ic_popup_sync), contentDescription = "Sync", tint = colors.primaryGlow)
                },
                onClick = { /* Trigger sync */ },
                enabled = syncEnabled
            )
        }

        // Data section
        SettingsSection(title = "Données") {
            SettingCard(
                title = "Effacer le cache",
                subtitle = "Libérer l'espace de stockage des images",
                trailing = {
                    Text(text = "247 MB", style = typography.bodySmall.copy(color = colors.contentTertiary))
                },
                onClick = { /* Clear cache */ }
            )

            SettingCard(
                title = "Exporter la bibliothèque",
                subtitle = "Sauvegarder en JSON/CSV",
                trailing = {
                    Icon(painterResource(android.R.drawable.ic_menu_save), contentDescription = "Exporter", tint = colors.contentTertiary)
                },
                onClick = { /* Export */ }
            )

            SettingCard(
                title = "Importer la bibliothèque",
                subtitle = "Restaurer depuis une sauvegarde",
                trailing = {
                    Icon(painterResource(android.R.drawable.ic_menu_upload), contentDescription = "Importer", tint = colors.contentTertiary)
                },
                onClick = { /* Import */ }
            )

            SettingCard(
                title = "Réinitialiser l'application",
                subtitle = "Supprimer toutes les données et préférences",
                trailing = {
                    Text(text = "⚠️", style = typography.bodyMedium)
                },
                onClick = { /* Show confirm dialog */ },
                destructive = true
            )
        }

        // About section
        SettingsSection(title = "À propos") {
            SettingCard(
                title = "Version",
                subtitle = "Komorebi 1.0.0",
                trailing = {
                    Text(text = "Build 1", style = typography.bodySmall.copy(color = colors.contentTertiary))
                }
            )

            SettingCard(
                title = "Licences open source",
                subtitle = "Voir les licences des bibliothèques utilisées",
                trailing = {
                    Icon(painterResource(android.R.drawable.ic_menu_info_details), contentDescription = "Licences", tint = colors.contentTertiary)
                },
                onClick = { /* Show licenses */ }
            )

            SettingCard(
                title = "Confidentialité",
                subtitle = "Politique de confidentialité",
                trailing = {
                    Icon(painterResource(android.R.drawable.ic_menu_info_details), contentDescription = "Privacy", tint = colors.contentTertiary)
                },
                onClick = { /* Open privacy policy */ }
            )

            SettingCard(
                title = "Signaler un bug",
                subtitle = "Nous aider à améliorer l'application",
                trailing = {
                    Icon(painterResource(android.R.drawable.ic_menu_report_image), contentDescription = "Bug report", tint = colors.contentTertiary)
                },
                onClick = { /* Open bug report */ }
            )

            SettingCard(
                title = "Soutenir le développement",
                subtitle = "Faire un don / Contribuer sur GitHub",
                trailing = {
                    Icon(painterResource(android.R.drawable.ic_menu_my_calendar), contentDescription = "Support", tint = colors.secondaryGlow)
                },
                onClick = { /* Open support */ }
            )
        }
    }
}

// ===== SETTINGS SECTION =====

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title.uppercase(),
            style = typography.labelMedium.copy(
                color = colors.primaryGlow,
                letterSpacing = 0.5.sp
            ),
            modifier = Modifier.padding(bottom = spacing.Small)
        )
        content()
    }
}

// ===== SETTING CARD =====

@Composable
fun SettingCard(
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    destructive: Boolean = false
) {
    val colors = komorebiColors()
    val typography = komorebiTypography()
    val spacing = KomorebiSpacing
    val shapes = komorebiShapes()

    val variant = if (destructive) com.komorebi.reader.ui.components.GlassComponents.GlassCardVariant.Outlined
    else com.komorebi.reader.ui.components.GlassComponents.GlassCardVariant.Standard

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f),
        variant = variant,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(spacing.Medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = typography.bodyMedium.copy(
                        color = if (destructive) colors.statusError else colors.contentPrimary
                    )
                )
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = spacing.XS))
                Text(
                    text = subtitle,
                    style = typography.bodySmall.copy(color = colors.contentTertiary),
                    maxLines = 2
                )
            }
            trailing()
        }
    }
}

// ===== SETTINGS ENUMS =====

enum class AppThemeMode(val label: String) {
    System("Système"),
    Light("Clair"),
    Dark("Sombre")
}

enum class ReadingDirection(val label: String) {
    RightToLeft("Manga (→←)"),
    LeftToRight("Comics (←→)"),
    Vertical("Webtoon (↓)"),
    VerticalRtl("Webtoon Manga (↓→←)")
}

enum class ReaderTheme(val label: String) {
    Dark("Sombre"),
    Light("Clair"),
    Sepia("Sépia")
}

enum class FontSize(val label: String) {
    Small("Petit"),
    Medium("Moyen"),
    Large("Grand"),
    ExtraLarge("Très grand")
}

enum class LineHeight(val label: String) {
    Tight("Compact"),
    Normal("Normal"),
    Relaxed("Aéré"),
    Loose("Très aéré")
}

enum class Margins(val label: String) {
    Narrow("Étroit"),
    Normal("Normal"),
    Wide("Large"),
    ExtraWide("Très large")
}

enum class SortOrder(val label: String) {
    RecentlyRead("Récemment lus"),
    Title("Titre (A-Z)"),
    Author("Auteur"),
    Progress("Progression"),
    AddedDate("Date d'ajout"),
    UpdatedDate("Dernière mise à jour")
}