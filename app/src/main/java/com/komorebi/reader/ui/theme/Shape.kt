package com.komorebi.reader.ui.theme

import androidx.compose.ui.geometry.CornerSize
import androidx.compose.ui.geometry.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Komorebi Premium Shape System
 *
 * Organic, flowing shapes inspired by nature
 * Asymmetric radii for liquid/glass morphism feel
 */

// ===== CORNER RADII SCALE =====
// Based on 4dp grid with organic variations
object KomorebiCornerRadii {
    // Standard rounded corners
    val None = 0.dp
    val XS = 4.dp
    val Small = 8.dp
    val Medium = 12.dp
    val Large = 16.dp
    val XL = 24.dp
    val XXL = 32.dp
    val Full = 9999.dp  // Fully rounded (pill/circle)

    // Liquid/organic asymmetric corners
    // Top-heavy (like a droplet)
    val DropletTop = CornerSize(Large, Large)
    val DropletBottom = CornerSize(XS, XS)

    // Bottom-heavy (like a settling liquid)
    val PuddleTop = CornerSize(XS, XS)
    val PuddleBottom = CornerSize(Large, Large)

    // Organic blob - slightly irregular
    val BlobTopStart = CornerSize(XL, XL)
    val BlobTopEnd = CornerSize(Large, Large)
    val BlobBottomEnd = CornerSize(Medium, Medium)
    val BlobBottomStart = CornerSize(XL, XL)

    // Glass card - subtle depth
    val GlassCard = CornerSize(Medium, Medium)

    // Floating action / liquid button
    val LiquidFAB = CornerSize(XXL, XXL)

    // Sheet/modal - friendly entry
    val SheetTop = CornerSize(XL, XL)
    val SheetBottom = CornerSize(None, None)

    // Reader page curl simulation
    val PageCurlTop = CornerSize(Small, Small)
    val PageCurlBottom = CornerSize(Large, Large)
}

// ===== SHAPE DEFINITIONS =====
object KomorebiShapes {

    // Symmetric shapes
    val None = RoundedCornerShape(KomorebiCornerRadii.None)
    val XS = RoundedCornerShape(KomorebiCornerRadii.XS)
    val Small = RoundedCornerShape(KomorebiCornerRadii.Small)
    val Medium = RoundedCornerShape(KomorebiCornerRadii.Medium)
    val Large = RoundedCornerShape(KomorebiCornerRadii.Large)
    val XL = RoundedCornerShape(KomorebiCornerRadii.XL)
    val XXL = RoundedCornerShape(KomorebiCornerRadii.XXL)
    val Full = RoundedCornerShape(KomorebiCornerRadii.Full)

    // Asymmetric / Organic shapes
    val Droplet = RoundedCornerShape(
        topStart = KomorebiCornerRadii.DropletTop,
        topEnd = KomorebiCornerRadii.DropletTop,
        bottomStart = KomorebiCornerRadii.DropletBottom,
        bottomEnd = KomorebiCornerRadii.DropletBottom
    )

    val Puddle = RoundedCornerShape(
        topStart = KomorebiCornerRadii.PuddleTop,
        topEnd = KomorebiCornerRadii.PuddleTop,
        bottomStart = KomorebiCornerRadii.PuddleBottom,
        bottomEnd = KomorebiCornerRadii.PuddleBottom
    )

    val Blob = RoundedCornerShape(
        topStart = KomorebiCornerRadii.BlobTopStart,
        topEnd = KomorebiCornerRadii.BlobTopEnd,
        bottomStart = KomorebiCornerRadii.BlobBottomStart,
        bottomEnd = KomorebiCornerRadii.BlobBottomEnd
    )

    // Component-specific shapes
    val GlassCard = RoundedCornerShape(KomorebiCornerRadii.GlassCard)
    val LiquidFAB = RoundedCornerShape(KomorebiCornerRadii.LiquidFAB)

    val BottomSheet = RoundedCornerShape(
        topStart = KomorebiCornerRadii.SheetTop,
        topEnd = KomorebiCornerRadii.SheetTop,
        bottomStart = KomorebiCornerRadii.SheetBottom,
        bottomEnd = KomorebiCornerRadii.SheetBottom
    )

    val TopSheet = RoundedCornerShape(
        topStart = KomorebiCornerRadii.SheetBottom,
        topEnd = KomorebiCornerRadii.SheetBottom,
        bottomStart = KomorebiCornerRadii.SheetTop,
        bottomEnd = KomorebiCornerRadii.SheetTop
    )

    val ReaderPage = RoundedCornerShape(KomerebiCornerRadii.Small)
    val ReaderPageCurl = RoundedCornerShape(
        topStart = KomorebiCornerRadii.PageCurlTop,
        topEnd = KomorebiCornerRadii.PageCurlTop,
        bottomStart = KomorebiCornerRadii.PageCurlBottom,
        bottomEnd = KomorebiCornerRadii.PageCurlBottom
    )

    // Navigation / chips
    val Pill = RoundedCornerShape(KomorebiCornerRadii.Full)
    val Chip = RoundedCornerShape(KomorebiCornerRadii.Full)

    // Image/Media containers
    val CoverImage = RoundedCornerShape(KomorebiCornerRadii.Medium)
    val Thumbnail = RoundedCornerShape(KomerebiCornerRadii.Small)
    val Avatar = RoundedCornerShape(KomerebiCornerRadii.Full)
}

// ===== SEMANTIC SHAPE SCHEME =====
@Immutable
data class KomorebiShapeScheme(
    // Base
    val none: RoundedCornerShape = KomorebiShapes.None,
    val xs: RoundedCornerShape = KomorebiShapes.XS,
    val small: RoundedCornerShape = KomorebiShapes.Small,
    val medium: RoundedCornerShape = KomorebiShapes.Medium,
    val large: RoundedCornerShape = KomorebiShapes.Large,
    val xl: RoundedCornerShape = KomorebiShapes.XL,
    val xxl: RoundedCornerShape = KomorebiShapes.XXL,
    val full: RoundedCornerShape = KomorebiShapes.Full,

    // Organic
    val droplet: RoundedCornerShape = KomorebiShapes.Droplet,
    val puddle: RoundedCornerShape = KomorebiShapes.Puddle,
    val blob: RoundedCornerShape = KomorebiShapes.Blob,

    // Components
    val glassCard: RoundedCornerShape = KomorebiShapes.GlassCard,
    val liquidFab: RoundedCornerShape = KomorebiShapes.LiquidFAB,
    val bottomSheet: RoundedCornerShape = KomorebiShapes.BottomSheet,
    val topSheet: RoundedCornerShape = KomorebiShapes.TopSheet,
    val readerPage: RoundedCornerShape = KomorebiShapes.ReaderPage,
    val readerPageCurl: RoundedCornerShape = KomorebiShapes.ReaderPageCurl,

    // Navigation
    val pill: RoundedCornerShape = KomorebiShapes.Pill,
    val chip: RoundedCornerShape = KomorebiShapes.Chip,

    // Media
    val coverImage: RoundedCornerShape = KomorebiShapes.CoverImage,
    val thumbnail: RoundedCornerShape = KomorebiShapes.Thumbnail,
    val avatar: RoundedCornerShape = KomorebiShapes.Avatar,
) {
    companion object {
        val Default = KomorebiShapeScheme()
    }
}