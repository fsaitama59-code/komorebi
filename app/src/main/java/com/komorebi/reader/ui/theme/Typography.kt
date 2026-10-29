package com.komorebi.reader.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

/**
 * Komorebi Premium Typography System
 *
 * A harmonious scale inspired by Japanese editorial design
 * Balancing readability with expressive character
 */

// ===== FONT FAMILIES =====
object KomorebiFonts {
    // Display/Heading - Elegant serif for titles (Noto Serif JP or custom)
    val Display = FontFamily(
        androidx.compose.ui.text.font.FontFamily.Default
    )

    // Body - Clean, readable sans (Inter, Noto Sans JP, or system)
    val Body = FontFamily(
        androidx.compose.ui.text.font.FontFamily.Default
    )

    // Mono - For code, page numbers, technical data
    val Mono = FontFamily(
        androidx.compose.ui.text.font.FontFamily.Monospace
    )

    // Accent - For special UI elements, chapter numbers
    val Accent = FontFamily(
        androidx.compose.ui.text.font.FontFamily.Default
    )
}

// ===== TYPE SCALE =====
// Based on 4px baseline grid, 1.25 major third ratio
object KomorebiTypeScale {

    // Display styles - For hero sections, major headings
    val DisplayLarge = TextStyle(
        fontFamily = KomorebiFonts.Display,
        fontWeight = FontWeight.W300,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = -0.25.sp,
    )

    val DisplayMedium = TextStyle(
        fontFamily = KomorebiFonts.Display,
        fontWeight = FontWeight.W300,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    )

    val DisplaySmall = TextStyle(
        fontFamily = KomorebiFonts.Display,
        fontWeight = FontWeight.W400,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    )

    // Headline styles - For section headers, card titles
    val HeadlineLarge = TextStyle(
        fontFamily = KomorebiFonts.Display,
        fontWeight = FontWeight.W400,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    )

    val HeadlineMedium = TextStyle(
        fontFamily = KomorebiFonts.Display,
        fontWeight = FontWeight.W500,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    )

    val HeadlineSmall = TextStyle(
        fontFamily = KomorebiFonts.Body,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    )

    // Title styles - For component titles, list items
    val TitleLarge = TextStyle(
        fontFamily = KomorebiFonts.Body,
        fontWeight = FontWeight.W600,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    )

    val TitleMedium = TextStyle(
        fontFamily = KomorebiFonts.Body,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    )

    val TitleSmall = TextStyle(
        fontFamily = KomorebiFonts.Body,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    )

    // Body styles - For reading content, descriptions
    val BodyLarge = TextStyle(
        fontFamily = KomorebiFonts.Body,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    )

    val BodyMedium = TextStyle(
        fontFamily = KomorebiFonts.Body,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    )

    val BodySmall = TextStyle(
        fontFamily = KomorebiFonts.Body,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    )

    // Label styles - For buttons, chips, tabs
    val LabelLarge = TextStyle(
        fontFamily = KomorebiFonts.Body,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    )

    val LabelMedium = TextStyle(
        fontFamily = KomorebiFonts.Body,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    )

    val LabelSmall = TextStyle(
        fontFamily = KomorebiFonts.Body,
        fontWeight = FontWeight.W500,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    )

    // Special reading styles - Optimized for long-form reading
    object Reading {
        // Main reading text - generous line height for comfort
        val Content = TextStyle(
            fontFamily = KomorebiFonts.Body,
            fontWeight = FontWeight.W400,
            fontSize = 17.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.1.sp,
        )

        val ContentLarge = TextStyle(
            fontFamily = KomorebiFonts.Body,
            fontWeight = FontWeight.W400,
            fontSize = 19.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.1.sp,
        )

        val ContentSmall = TextStyle(
            fontFamily = KomorebiFonts.Body,
            fontWeight = FontWeight.W400,
            fontSize = 15.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.1.sp,
        )

        // Chapter/Volume headers in reader
        val ChapterTitle = TextStyle(
            fontFamily = KomorebiFonts.Display,
            fontWeight = FontWeight.W300,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = -0.1.sp,
            textAlign = TextAlign.Center,
        )

        val ChapterNumber = TextStyle(
            fontFamily = KomorebiFonts.Accent,
            fontWeight = FontWeight.W700,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center,
        )

        // Manga-specific: vertical text support placeholder
        val VerticalText = TextStyle(
            fontFamily = KomorebiFonts.Display,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        )

        // UI overlay in reader (page numbers, progress)
        val Overlay = TextStyle(
            fontFamily = KomorebiFonts.Mono,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        )

        val OverlayLarge = TextStyle(
            fontFamily = KomorebiFonts.Mono,
            fontWeight = FontWeight.W600,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.5.sp,
        )
    }

    // Japanese text optimized styles
    object Japanese {
        val DisplayLarge = TextStyle(
            fontFamily = KomorebiFonts.Display,
            fontWeight = FontWeight.W300,
            fontSize = 52.sp,
            lineHeight = 60.sp,
        )

        val BodyLarge = TextStyle(
            fontFamily = KomorebiFonts.Body,
            fontWeight = FontWeight.W400,
            fontSize = 17.sp,
            lineHeight = 28.sp,
        )

        val BodyMedium = TextStyle(
            fontFamily = KomorebiFonts.Body,
            fontWeight = FontWeight.W400,
            fontSize = 15.sp,
            lineHeight = 26.sp,
        )

        val Ruby = TextStyle(
            fontFamily = KomorebiFonts.Body,
            fontWeight = FontWeight.W400,
            fontSize = 10.sp,
            lineHeight = 14.sp,
        )
    }
}

// ===== SEMANTIC TYPOGRAPHY =====
@Immutable
data class KomorebiTypography(
    // Display
    val displayLarge: TextStyle = KomorebiTypeScale.DisplayLarge,
    val displayMedium: TextStyle = KomorebiTypeScale.DisplayMedium,
    val displaySmall: TextStyle = KomorebiTypeScale.DisplaySmall,

    // Headline
    val headlineLarge: TextStyle = KomorebiTypeScale.HeadlineLarge,
    val headlineMedium: TextStyle = KomorebiTypeScale.HeadlineMedium,
    val headlineSmall: TextStyle = KomorebiTypeScale.HeadlineSmall,

    // Title
    val titleLarge: TextStyle = KomorebiTypeScale.TitleLarge,
    val titleMedium: TextStyle = KomorebiTypeScale.TitleMedium,
    val titleSmall: TextStyle = KomorebiTypeScale.TitleSmall,

    // Body
    val bodyLarge: TextStyle = KomorebiTypeScale.BodyLarge,
    val bodyMedium: TextStyle = KomorebiTypeScale.BodyMedium,
    val bodySmall: TextStyle = KomorebiTypeScale.BodySmall,

    // Label
    val labelLarge: TextStyle = KomorebiTypeScale.LabelLarge,
    val labelMedium: TextStyle = KomorebiTypeScale.LabelMedium,
    val labelSmall: TextStyle = KomorebiTypeScale.LabelSmall,

    // Reading-specific
    val readingContent: TextStyle = KomorebiTypeScale.Reading.Content,
    val readingContentLarge: TextStyle = KomorebiTypeScale.Reading.ContentLarge,
    val readingContentSmall: TextStyle = KomorebiTypeScale.Reading.ContentSmall,
    val readingChapterTitle: TextStyle = KomorebiTypeScale.Reading.ChapterTitle,
    val readingChapterNumber: TextStyle = KomorebiTypeScale.Reading.ChapterNumber,
    val readingOverlay: TextStyle = KomorebiTypeScale.Reading.Overlay,
    val readingOverlayLarge: TextStyle = KomorebiTypeScale.Reading.OverlayLarge,

    // Japanese optimized
    val japaneseDisplayLarge: TextStyle = KomorebiTypeScale.Japanese.DisplayLarge,
    val japaneseBodyLarge: TextStyle = KomorebiTypeScale.Japanese.BodyLarge,
    val japaneseBodyMedium: TextStyle = KomorebiTypeScale.Japanese.BodyMedium,
    val japaneseRuby: TextStyle = KomorebiTypeScale.Japanese.Ruby,
) {
    companion object {
        val Default = KomorebiTypography()
    }
}