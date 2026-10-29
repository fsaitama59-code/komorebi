package com.komorebi.reader.ui.theme

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.komorebi.reader.data.preferences.PreferencesRepository
import kotlinx.coroutines.launch

/**
 * Komorebi Premium Theme System
 *
 * Complete Material 3 integration with custom liquid/glass design tokens
 * Supports dynamic theming, system bars integration, and premium animations
 */

// ===== MATERIAL 3 COLOR SCHEME BRIDGE =====
private val DarkMaterialScheme = darkColorScheme(
    primary = KomorebiBrand.PrimaryGlow,
    onPrimary = KomorebiColors.Content.Inverse,
    primaryContainer = KomorebiBrand.Primary,
    onPrimaryContainer = KomorebiColors.Content.Primary,
    secondary = KomorebiBrand.SecondaryLight,
    onSecondary = KomorebiColors.Content.Inverse,
    secondaryContainer = KomorebiBrand.SecondaryVariant,
    onSecondaryContainer = KomorebiColors.Content.Primary,
    tertiary = KomorebiBrand.TertiaryLight,
    onTertiary = KomorebiColors.Content.Inverse,
    tertiaryContainer = KomorebiBrand.Tertiary,
    onTertiaryContainer = KomorebiColors.Content.Primary,
    error = KomorebiColors.Status.Error,
    onError = KomorebiColors.Content.Inverse,
    errorContainer = KomorebiColors.Status.ErrorContainer,
    onErrorContainer = KomorebiColors.Content.Primary,
    surface = KomorebiColors.Surface.Base,
    onSurface = KomorebiColors.Content.Primary,
    surfaceContainerLowest = KomorebiColors.Surface.Base,
    surfaceContainerLow = KomorebiColors.Surface.Elevated,
    surfaceContainer = KomorebiColors.Surface.Elevated,
    surfaceContainerHigh = KomorebiColors.Surface.Overlay,
    surfaceContainerHighest = KomorebiColors.Surface.Overlay,
    onSurfaceVariant = KomorebiColors.Content.Secondary,
    outline = KomorebiColors.Border.Glass,
    outlineVariant = KomorebiColors.Border.GlassDim,
    shadow = Color.Black,
    scrim = Color.Black,
    inverseSurface = KomorebiColors.Content.Primary,
    inverseOnSurface = KomorebiColors.Surface.Base,
    inversePrimary = KomorebiBrand.Primary,
)

private val LightMaterialScheme = lightColorScheme(
    primary = KomorebiBrand.Primary,
    onPrimary = KomorebiColors.Content.Inverse,
    primaryContainer = KomorebiBrand.PrimaryLight,
    onPrimaryContainer = KomorebiColors.Content.Inverse,
    secondary = KomorebiBrand.Secondary,
    onSecondary = KomorebiColors.Content.Inverse,
    secondaryContainer = KomorebiBrand.SecondaryLight,
    onSecondaryContainer = KomorebiColors.Content.Inverse,
    tertiary = KomorebiBrand.Tertiary,
    onTertiary = KomorebiColors.Content.Inverse,
    tertiaryContainer = KomorebiBrand.TertiaryLight,
    onTertiaryContainer = KomorebiColors.Content.Inverse,
    error = KomorebiColors.Status.Error,
    onError = KomorebiColors.Content.Inverse,
    errorContainer = KomorebiColors.Status.ErrorContainer,
    onErrorContainer = KomorebiColors.Content.Inverse,
    surface = KomorebiColorScheme.Light.surfaceBase,
    onSurface = KomorebiColorScheme.Light.contentPrimary,
    surfaceContainerLowest = KomorebiColorScheme.Light.surfaceBase,
    surfaceContainerLow = KomorebiColorScheme.Light.surfaceElevated,
    surfaceContainer = KomorebiColorScheme.Light.surfaceElevated,
    surfaceContainerHigh = KomorebiColorScheme.Light.surfaceOverlay,
    surfaceContainerHighest = KomorebiColorScheme.Light.surfaceOverlay,
    onSurfaceVariant = KomorebiColorScheme.Light.contentSecondary,
    outline = KomorebiColorScheme.Light.borderGlass,
    outlineVariant = KomorebiColorScheme.Light.borderGlassDim,
    shadow = Color.Black,
    scrim = Color.Black,
    inverseSurface = KomorebiColorScheme.Light.contentPrimary,
    inverseOnSurface = KomorebiColorScheme.Light.surfaceBase,
    inversePrimary = KomorebiBrand.PrimaryGlow,
)

// ===== THEME ENTRY POINT =====
@Composable
fun KomorebiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Observe user preference for theme
    val preferencesRepo = PreferencesRepository(context)
    val themeMode by preferencesRepo.themeMode.observeAsState(ThemeMode.System)

    val effectiveDarkTheme = when (themeMode) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> darkTheme
    }

    val colorScheme = if (effectiveDarkTheme) DarkMaterialScheme else LightMaterialScheme
    val komorebiColors = if (effectiveDarkTheme) KomorebiColorScheme.Default else KomorebiColorScheme.Light

    // System bars integration
    SideEffect {
        val window = (context as Activity).window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowInsetsControllerCompat(window, view)
        insetsController.isAppearanceLightStatusBars = !effectiveDarkTheme
        insetsController.isAppearanceLightNavigationBars = !effectiveDarkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KomorebiTypography.Default.toMaterial3(),
        shapes = KomorebiShapeScheme.Default.toMaterial3(),
        content = content
    )
}

// ===== THEME MODE ENUM =====
enum class ThemeMode {
    System, Light, Dark
}

// ===== EXTENSION PROPERTIES FOR EASY ACCESS =====
@Composable
fun komorebiColors(): KomorebiColorScheme {
    val isDark = MaterialTheme.colorScheme.isDark
    return if (isDark) KomorebiColorScheme.Default else KomorebiColorScheme.Light
}

@Composable
fun komorebiTypography(): KomorebiTypography = KomorebiTypography.Default

@Composable
fun komorebiShapes(): KomorebiShapeScheme = KomorebiShapeScheme.Default

// ===== CONVERSION TO MATERIAL 3 =====
private fun KomorebiTypography.toMaterial3() = androidx.compose.material3.Typography(
    displayLarge = displayLarge,
    displayMedium = displayMedium,
    displaySmall = displaySmall,
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall,
)

private fun KomorebiShapeScheme.toMaterial3() = androidx.compose.material3.Shapes(
    extraSmall = xs,
    small = small,
    medium = medium,
    large = large,
    extraLarge = xl,
)

// ===== PREMIUM GLASS/LIQUID EFFECTS =====
object GlassEffects {

    // Standard glass elevation shadows
    val GlassElevation1 = listOf(
        Shadow(0.dp, 1.dp, komorebiColors().surfaceGlassShadow.copy(alpha = 0.15f)),
        Shadow(0.dp, 2.dp, komorebiColors().surfaceGlassShadow.copy(alpha = 0.1f)),
    )

    val GlassElevation2 = listOf(
        Shadow(0.dp, 4.dp, komorebiColors().surfaceGlassShadow.copy(alpha = 0.2f)),
        Shadow(0.dp, 8.dp, komorebiColors().surfaceGlassShadow.copy(alpha = 0.15f)),
        Shadow(0.dp, 16.dp, komorebiColors().surfaceGlassShadow.copy(alpha = 0.1f)),
    )

    val GlassElevation3 = listOf(
        Shadow(0.dp, 8.dp, komorebiColors().surfaceGlassShadow.copy(alpha = 0.25f)),
        Shadow(0.dp, 24.dp, komorebiColors().surfaceGlassShadow.copy(alpha = 0.2f)),
        Shadow(0.dp, 32.dp, komorebiColors().surfaceGlassShadow.copy(alpha = 0.15f)),
    )

    // Liquid morphing shadows
    val LiquidAmbient = listOf(
        Shadow(0.dp, 0.dp, komorebiColors().primaryGlow.copy(alpha = 0.08f), blurRadius = 40.dp),
    )

    val LiquidGlow = listOf(
        Shadow(0.dp, 0.dp, komorebiColors().primaryGlow.copy(alpha = 0.15f), blurRadius = 60.dp),
        Shadow(0.dp, 0.dp, komorebiColors().secondaryGlow.copy(alpha = 0.1f), blurRadius = 80.dp),
    )

    // Inner glow for glass surfaces
    fun DrawScope.drawInnerGlow(
        color: Color = komorebiColors().surfaceGlassHighlight,
        radius: Float = 1f
    ) {
        drawRect(
            color = color,
            style = androidx.compose.ui.draw.Stroke(width = radius),
        )
    }

    // Shimmer sweep animation helper
    @Composable
    fun ShimmerSweep(
        modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
        colors: List<Color> = KomorebiColors.Gradients.GlassShimmer,
        durationMs: Int = 2000,
        delayMs: Int = 0
    ) {
        // Implementation in GlassComponents.kt
    }
}

// ===== ANIMATION SPECIFICATIONS =====
object KomorebiMotion {

    // Easing curves - organic, liquid feel
    val LiquidEaseOut = androidx.compose.animation.core.Easing(androidx.compose.animation.core.cubicBezier(0.25, 0.46, 0.45, 0.94))
    val LiquidEaseIn = androidx.compose.animation.core.Easing(androidx.compose.animation.core.cubicBezier(0.55, 0.06, 0.68, 0.19))
    val LiquidEaseInOut = androidx.compose.animation.core.Easing(androidx.compose.animation.core.cubicBezier(0.42, 0.0, 0.58, 1.0))
    val SpringGentle = androidx.compose.animation.core.spring(dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy, stiffness = androidx.compose.animation.core.Spring.StiffnessLow)
    val SpringSharp = androidx.compose.animation.core.spring(dampingRatio = androidx.compose.animation.core.Spring.DampingRatioHighBouncy, stiffness = androidx.compose.animation.core.Spring.StiffnessMedium)
    val SpringWobbly = androidx.compose.animation.core.spring(dampingRatio = androidx.compose.animation.core.Spring.DampingRatioLowBouncy, stiffness = androidx.compose.animation.core.Spring.StiffnessVeryLow)

    // Duration tokens
    val DurationInstant = 50
    val DurationFast = 150
    val DurationMedium = 250
    val DurationSlow = 400
    val DurationSlowest = 600
    val DurationLiquid = 800
    val DurationAmbient = 3000

    // Standard tween specs
    val TweenFast = androidx.compose.animation.core.tween(DurationFast, easing = LiquidEaseOut)
    val TweenMedium = androidx.compose.animation.core.tween(DurationMedium, easing = LiquidEaseOut)
    val TweenSlow = androidx.compose.animation.core.tween(DurationSlow, easing = LiquidEaseInOut)
    val TweenLiquid = androidx.compose.animation.core.tween(DurationLiquid, easing = LiquidEaseInOut)

    // Spring specs
    val SpringEnter = SpringGentle
    val SpringExit = SpringSharp
    val SpringWobble = SpringWobbly
}

// ===== SPACING SYSTEM =====
object KomorebiSpacing {
    val None = 0.dp
    val XS = 4.dp
    val Small = 8.dp
    val Medium = 16.dp
    val Large = 24.dp
    val XL = 32.dp
    val XXL = 48.dp
    val XXXL = 64.dp

    // Semantic spacing
    val ComponentPadding = Medium
    val ScreenPadding = Large
    val CardPadding = Medium
    val ListItemGap = Small
    val SectionGap = XL
    val InsetSmall = Small
    val InsetMedium = Medium
    val InsetLarge = Large
}

// ===== Z-INDEX LAYERS =====
object KomorebiZIndex {
    val Base = 0
    val Content = 100
    val GlassOverlay = 200
    val FloatingAction = 300
    val Header = 400
    val Modal = 500
    val Toast = 600
    val Dialog = 700
    val BottomSheet = 800
    val LoadingOverlay = 900
    val SystemAlert = 1000
}