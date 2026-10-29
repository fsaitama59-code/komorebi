package com.komorebi.reader.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Komorebi Premium Color System
 *
 * Inspired by "komorebi" (木漏れ日) - sunlight filtering through trees
 * A liquid/glass morphism design with depth, translucency, and organic flow
 */

// ===== CORE BRAND COLORS =====
object KomorebiBrand {
    // Primary - Deep forest emerald with golden hour warmth
    val Primary = Color(0xFF0D4A3C)
    val PrimaryVariant = Color(0xFF08352A)
    val PrimaryLight = Color(0xFF1A6B56)
    val PrimaryGlow = Color(0xFF2EB88A)

    // Secondary - Warm amber/gold for accents
    val Secondary = Color(0xFFC8962E)
    val SecondaryVariant = Color(0xFF9B721F)
    val SecondaryLight = Color(0xFFE8B95A)
    val SecondaryGlow = Color(0xFFFFD47A)

    // Tertiary - Soft petal pink for delicate accents
    val Tertiary = Color(0xFFB87A8A)
    val TertiaryLight = Color(0xFFD4A5B0)
}

// ===== SEMANTIC COLOR ROLES =====
object KomorebiColors {

    // Surface hierarchy - Glass morphism layers
    object Surface {
        // Base surfaces (opaque)
        val Base = Color(0xFF0A0F0E)           // Deepest night
        val Elevated = Color(0xFF111A18)       // Slightly elevated
        val Overlay = Color(0xFF16221F)        // Modal/drawer backgrounds

        // Glass surfaces (translucent with blur)
        val Glass = Color(0x801A2A25)          // 50% - Primary glass
        val GlassStrong = Color(0xA01A2A25)    // 62% - Stronger glass
        val GlassSubtle = Color(0x401A2A25)    // 25% - Subtle glass
        val GlassHighlight = Color(0x30FFFFFF) // 18% - Highlight edge
        val GlassShadow = Color(0x60000000)    // 37% - Depth shadow
    }

    // Content colors
    object Content {
        val Primary = Color(0xFFF5F5F0)        // Warm white
        val Secondary = Color(0xFFB8C8C0)      // Muted sage
        val Tertiary = Color(0xFF8A9A92)       // Subtle text
        val Disabled = Color(0xFF5A6A62)       // Disabled state
        val Inverse = Color(0xFF0A0F0E)        // On colored backgrounds
        val Link = KomorebiBrand.PrimaryGlow
        val LinkPressed = KomorebiBrand.SecondaryLight
    }

    // Glass border system
    object Border {
        val Glass = Color(0x30FFFFFF)          // Subtle glass border
        val GlassBright = Color(0x50FFFFFF)    // Bright catch light
        val GlassDim = Color(0x1AFFFFFF)       // Very subtle
        val Accent = KomorebiBrand.Secondary   // Accent border
        val AccentGlow = KomorebiBrand.SecondaryGlow
        val Error = Color(0xFFE86C6C)
        val Success = KomorebiBrand.PrimaryGlow
        val Warning = KomorebiBrand.Secondary
    }

    // State layers
    object State {
        val Hover = Color(0x0FFFFFFF)          // 6%
        val Focus = Color(0x14FFFFFF)          // 8%
        val Pressed = Color(0x1FFFFFFF)        // 12%
        val Dragged = Color(0x14FFFFFF)        // 8%
        val Selected = Color(0x262EB88A)       // 15% primary
        val Activated = Color(0x1AFFFFFF)      // 10%
    }

    // Status colors with glass variants
    object Status {
        val Error = Color(0xFFE86C6C)
        val ErrorContainer = Color(0x40E86C6C)
        val Success = Color(0xFF2EB88A)
        val SuccessContainer = Color(0x402EB88A)
        val Warning = Color(0xFFFFB84D)
        val WarningContainer = Color(0x40FFB84D)
        val Info = Color(0xFF64B5F6)
        val InfoContainer = Color(0x4064B5F6)
    }

    // Gradient definitions for liquid effects
    object Gradients {
        val PrimaryFlow = listOf(
            KomorebiBrand.Primary,
            KomorebiBrand.PrimaryLight,
            KomorebiBrand.PrimaryGlow
        )

        val SecondaryFlow = listOf(
            KomorebiBrand.SecondaryVariant,
            KomorebiBrand.Secondary,
            KomorebiBrand.SecondaryLight
        )

        val GlassShimmer = listOf(
            Color(0x00FFFFFF),
            Color(0x30FFFFFF),
            Color(0x00FFFFFF)
        )

        val LiquidAurora = listOf(
            Color(0xFF0D4A3C),
            Color(0xFF1A6B56),
            Color(0xFF2EB88A),
            Color(0xFFC8962E),
            Color(0xFFE8B95A)
        )

        val SurfaceDepth = listOf(
            Color(0xFF0A0F0E),
            Color(0xFF111A18),
            Color(0xFF16221F)
        )
    }
}

// ===== DYNAMIC COLOR SCHEMES =====
@Immutable
data class KomorebiColorScheme(
    // Brand
    val primary: Color = KomorebiBrand.Primary,
    val primaryVariant: Color = KomorebiBrand.PrimaryVariant,
    val primaryLight: Color = KomorebiBrand.PrimaryLight,
    val primaryGlow: Color = KomorebiBrand.PrimaryGlow,
    val secondary: Color = KomorebiBrand.Secondary,
    val secondaryVariant: Color = KomorebiBrand.SecondaryVariant,
    val secondaryLight: Color = KomorebiBrand.SecondaryLight,
    val secondaryGlow: Color = KomorebiBrand.SecondaryGlow,
    val tertiary: Color = KomorebiBrand.Tertiary,
    val tertiaryLight: Color = KomorebiBrand.TertiaryLight,

    // Surfaces
    val surfaceBase: Color = KomorebiColors.Surface.Base,
    val surfaceElevated: Color = KomorebiColors.Surface.Elevated,
    val surfaceOverlay: Color = KomorebiColors.Surface.Overlay,
    val surfaceGlass: Color = KomorebiColors.Surface.Glass,
    val surfaceGlassStrong: Color = KomorebiColors.Surface.GlassStrong,
    val surfaceGlassSubtle: Color = KomorebiColors.Surface.GlassSubtle,
    val surfaceGlassHighlight: Color = KomorebiColors.Surface.GlassHighlight,
    val surfaceGlassShadow: Color = KomorebiColors.Surface.GlassShadow,

    // Content
    val contentPrimary: Color = KomorebiColors.Content.Primary,
    val contentSecondary: Color = KomorebiColors.Content.Secondary,
    val contentTertiary: Color = KomorebiColors.Content.Tertiary,
    val contentDisabled: Color = KomorebiColors.Content.Disabled,
    val contentInverse: Color = KomorebiColors.Content.Inverse,
    val contentLink: Color = KomorebiColors.Content.Link,

    // Borders
    val borderGlass: Color = KomorebiColors.Border.Glass,
    val borderGlassBright: Color = KomorebiColors.Border.GlassBright,
    val borderGlassDim: Color = KomorebiColors.Border.GlassDim,
    val borderAccent: Color = KomorebiColors.Border.Accent,
    val borderAccentGlow: Color = KomorebiColors.Border.AccentGlow,
    val borderError: Color = KomorebiColors.Border.Error,
    val borderSuccess: Color = KomorebiColors.Border.Success,
    val borderWarning: Color = KomorebiColors.Border.Warning,

    // States
    val stateHover: Color = KomorebiColors.State.Hover,
    val stateFocus: Color = KomorebiColors.State.Focus,
    val statePressed: Color = KomorebiColors.State.Pressed,
    val stateDragged: Color = KomorebiColors.State.Dragged,
    val stateSelected: Color = KomorebiColors.State.Selected,
    val stateActivated: Color = KomorebiColors.State.Activated,

    // Status
    val statusError: Color = KomorebiColors.Status.Error,
    val statusErrorContainer: Color = KomorebiColors.Status.ErrorContainer,
    val statusSuccess: Color = KomorebiColors.Status.Success,
    val statusSuccessContainer: Color = KomorebiColors.Status.SuccessContainer,
    val statusWarning: Color = KomorebiColors.Status.Warning,
    val statusWarningContainer: Color = KomorebiColors.Status.WarningContainer,
    val statusInfo: Color = KomorebiColors.Status.Info,
    val statusInfoContainer: Color = KomorebiColors.Status.InfoContainer,
) {
    // Light theme variant (for user preference)
    companion object {
        val Light = KomorebiColorScheme(
            primary = Color(0xFF0D4A3C),
            primaryVariant = Color(0xFF08352A),
            primaryLight = Color(0xFF1A6B56),
            primaryGlow = Color(0xFF2EB88A),
            secondary = Color(0xFFB8860B),
            secondaryVariant = Color(0xFF8B6914),
            secondaryLight = Color(0xFFDAA520),
            secondaryGlow = Color(0xFFFFD700),
            tertiary = Color(0xFFB87A8A),
            tertiaryLight = Color(0xFFD4A5B0),

            surfaceBase = Color(0xFFF8FAF9),
            surfaceElevated = Color(0xFFFFFFFF),
            surfaceOverlay = Color(0xFFF0F5F2),
            surfaceGlass = Color(0xC0FFFFFF),
            surfaceGlassStrong = Color(0xE0FFFFFF),
            surfaceGlassSubtle = Color(0x80FFFFFF),
            surfaceGlassHighlight = Color(0x40000000),
            surfaceGlassShadow = Color(0x30000000),

            contentPrimary = Color(0xFF0A0F0E),
            contentSecondary = Color(0xFF3A4A42),
            contentTertiary = Color(0xFF6A7A72),
            contentDisabled = Color(0xFF9A9A9A),
            contentInverse = Color(0xFFFFFFFF),
            contentLink = Color(0xFF0D4A3C),

            borderGlass = Color(0x40000000),
            borderGlassBright = Color(0x60000000),
            borderGlassDim = Color(0x20000000),
            borderAccent = Color(0xFFB8860B),
            borderAccentGlow = Color(0xFFFFD700),
            borderError = Color(0xFFDC3545),
            borderSuccess = Color(0xFF2EB88A),
            borderWarning = Color(0xFFFFB84D),
        )
    }
}