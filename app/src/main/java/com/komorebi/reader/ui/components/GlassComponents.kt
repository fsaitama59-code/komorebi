package com.komorebi.reader.ui.components

import androidx.compose.animation.animateFloatAsState
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.pointerInput
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.CombinedTransitionSpec
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.px
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.komorebi.reader.ui.theme.GlassEffects
import com.komorebi.reader.ui.theme.KomorebiColorScheme
import com.komorebi.reader.ui.theme.KomorebiMotion
import com.komorebi.reader.ui.theme.KomorebiShapeScheme
import com.komorebi.reader.ui.theme.KomorebiSpacing
import com.komorebi.reader.ui.theme.KomorebiTypography
import com.komorebi.reader.ui.theme.komorebiColors
import com.komorebi.reader.ui.theme.komorebiShapes
import com.komorebi.reader.ui.theme.komorebiTypography
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Komorebi Premium Glass & Liquid Components
 *
 * The heart of the liquid/glass morphism design system.
 * Each component embodies depth, translucency, and organic motion.
 */

// ========================================================================
// GLASS SURFACE - Base glass container with blur simulation
// ========================================================================

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    variant: GlassVariant = GlassVariant.Standard,
    shape: RoundedCornerShape = komorebiShapes().glassCard,
    content: @Composable BoxScope.() -> Unit
) {
    val colors = komorebiColors()

    val (surfaceColor, borderColor, borderWidth, elevation) = when (variant) {
        GlassVariant.Subtle -> colors.surfaceGlassSubtle to colors.borderGlassDim to 0.5.dp to GlassEffects.GlassElevation1
        GlassVariant.Standard -> colors.surfaceGlass to colors.borderGlass to 1.dp to GlassEffects.GlassElevation2
        GlassVariant.Strong -> colors.surfaceGlassStrong to colors.borderGlassBright to 1.5.dp to GlassEffects.GlassElevation3
        GlassVariant.Glow -> colors.surfaceGlass to colors.borderAccentGlow to 2.dp to GlassEffects.LiquidGlow
    }

    Box(
        modifier = modifier
            .background(surfaceColor, shape)
            .border(borderWidth, borderColor, shape)
            .shadow(elevation = elevation, shape = shape),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

enum class GlassVariant {
    Subtle, Standard, Strong, Glow
}

// ========================================================================
// LIQUID BUTTON - Morphing, organic button with liquid press animation
// ========================================================================

@Composable
fun LiquidButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: LiquidButtonVariant = LiquidButtonVariant.Primary,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    val colors = komorebiColors()
    val shapes = komorebiShapes()
    val typography = komorebiTypography()

    var pressed by remember { mutableStateOf(false) }
    var hovered by remember { mutableStateOf(false) }
    val pressProgress by animateFloatAsState(
        targetValue = if (pressed) 1f else 0f,
        animationSpec = KomorebiMotion.SpringWobble
    )
    val hoverProgress by animateFloatAsState(
        targetValue = if (hovered) 1f else 0f,
        animationSpec = KomorebiMotion.TweenFast
    )

    val (bgColor, contentColor, borderColor, glowColor) = when (variant) {
        LiquidButtonVariant.Primary -> {
            val baseBg = colors.primary
            val pressedBg = colors.primaryLight
            val hoverBg = colors.primaryGlow.copy(alpha = 0.3f)
            baseBg to colors.contentInverse to colors.borderGlass to colors.primaryGlow
        }
        LiquidButtonVariant.Secondary -> {
            colors.secondary to colors.contentInverse to colors.borderAccent to colors.secondaryGlow
        }
        LiquidButtonVariant.Glass -> {
            colors.surfaceGlass to colors.contentPrimary to colors.borderGlassBright to colors.primaryGlow
        }
        LiquidButtonVariant.Ghost -> {
            Color.Transparent to colors.contentPrimary to colors.borderGlass to colors.primaryGlow
        }
        LiquidButtonVariant.Destructive -> {
            colors.statusError to colors.contentInverse to colors.borderError to colors.statusError
        }
    }

    val effectiveBg = if (!enabled) colors.surfaceGlassSubtle
    else if (pressed) bgColor.copy(alpha = 0.8f)
    else if (hovered) bgColor.copy(alpha = 0.9f)
    else bgColor

    GlassSurface(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { pressed = true },
                    onRelease = { pressed = false; if (enabled) onClick() },
                    onCancel = { pressed = false }
                )
            }
            .combinedClickable(
                onClick = { if (enabled) onClick() },
                onLongClick = {},
                enabled = enabled,
                role = androidx.compose.ui.semantics.Role.Button
            ),
        variant = GlassVariant.Standard,
        shape = shapes.liquidFab
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(KomorebiSpacing.Medium)
                .graphicsLayer {
                    scaleX = 1f - pressProgress * 0.02f
                    scaleY = 1f - pressProgress * 0.02f
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.invoke()
            if (leadingIcon != null) androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(KomorebiSpacing.Small))
            content()
            if (trailingIcon != null) androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(KomorebiSpacing.Small))
            trailingIcon?.invoke()
        }
    }
}

enum class LiquidButtonVariant {
    Primary, Secondary, Glass, Ghost, Destructive
}

// ========================================================================
// LIQUID FAB - Floating action button with liquid morphing
// ========================================================================

@Composable
fun LiquidFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    extended: Boolean = false,
    icon: @Composable () -> Unit,
    label: @Composable (() -> Unit)? = null
) {
    val colors = komorebiColors()
    val shapes = komorebiShapes()

    var pressed by remember { mutableStateOf(false) }
    val pressProgress by animateFloatAsState(
        targetValue = if (pressed) 1f else 0f,
        animationSpec = KomorebiMotion.SpringWobble
    )

    GlassSurface(
        modifier = modifier
            .size(if (extended) 56.dp else 56.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { pressed = true },
                    onRelease = { pressed = false; onClick() },
                    onCancel = { pressed = false }
                )
            }
            .combinedClickable(onClick = onClick, onLongClick = {}, role = androidx.compose.ui.semantics.Role.Button),
        variant = GlassVariant.Glow,
        shape = shapes.liquidFab
    ) {
        if (extended && label != null) {
            Row(
                modifier = Modifier.padding(horizontal = KomorebiSpacing.Large),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon()
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(KomorebiSpacing.Small))
                label()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { scaleX = 1f - pressProgress * 0.1f; scaleY = 1f - pressProgress * 0.1f },
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
        }
    }
}

// ========================================================================
// GLASS CARD - Premium content card with liquid hover/tap
// ========================================================================

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    variant: GlassCardVariant = GlassCardVariant.Standard,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = komorebiColors()
    val shapes = komorebiShapes()

    var pressed by remember { mutableStateOf(false) }
    var hovered by remember { mutableStateOf(false) }
    val pressProgress by animateFloatAsState(
        targetValue = if (pressed) 1f else 0f,
        animationSpec = KomorebiMotion.SpringWobble
    )
    val hoverProgress by animateFloatAsState(
        targetValue = if (hovered) 1f else 0f,
        animationSpec = KomorebiMotion.TweenMedium
    )

    val glassVariant = when (variant) {
        GlassCardVariant.Standard -> GlassVariant.Standard
        GlassCardVariant.Elevated -> GlassVariant.Strong
        GlassCardVariant.Outlined -> GlassVariant.Subtle
        GlassCardVariant.Glow -> GlassVariant.Glow
    }

    GlassSurface(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { if (onClick != null) pressed = true },
                    onRelease = { if (onClick != null) { pressed = false; onClick() } },
                    onCancel = { pressed = false }
                )
            }
            .graphicsLayer {
                scaleX = 1f - pressProgress * 0.01f
                scaleY = 1f - pressProgress * 0.01f
                translationZ = hoverProgress * 8f + pressProgress * 4f
            }
            .combinedClickable(
                onClick = { onClick?.invoke() },
                onLongClick = {},
                enabled = onClick != null,
                role = if (onClick != null) androidx.compose.ui.semantics.Role.Button else null
            ),
        variant = glassVariant,
        shape = shapes.glassCard
    ) {
        Column(modifier = Modifier.padding(KomorebiSpacing.Medium)) {
            if (leading != null || trailing != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    leading?.invoke()
                    trailing?.invoke()
                }
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(bottom = KomorebiSpacing.Small))
            }
            content()
        }
    }
}

enum class GlassCardVariant {
    Standard, Elevated, Outlined, Glow
}

// ========================================================================
// LIQUID CHIP - Organic pill-shaped chip with fluid selection
// ========================================================================

@Composable
fun LiquidChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    variant: LiquidChipVariant = LiquidChipVariant.Standard
) {
    val colors = komorebiColors()
    val shapes = komorebiShapes()
    val typography = komorebiTypography()

    val selectProgress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = KomorebiMotion.SpringGentle
    )

    val (bgColor, contentColor, borderColor) = when (variant) {
        LiquidChipVariant.Standard -> {
            val bg = colors.surfaceGlass.lerp(colors.primary, selectProgress)
            val content = colors.contentPrimary.lerp(colors.contentInverse, selectProgress)
            val border = colors.borderGlass.lerp(colors.borderAccent, selectProgress)
            bg to content to border
        }
        LiquidChipVariant.Tonal -> {
            val bg = colors.surfaceGlassStrong.lerp(colors.secondary.copy(alpha = 0.3f), selectProgress)
            val content = colors.contentSecondary.lerp(colors.secondaryVariant, selectProgress)
            val border = colors.borderGlassDim.lerp(colors.borderAccent, selectProgress)
            bg to content to border
        }
        LiquidChipVariant.Outline -> {
            val bg = colors.surfaceGlassSubtle
            val content = colors.contentSecondary.lerp(colors.primary, selectProgress)
            val border = colors.borderGlass.lerp(colors.borderAccent, selectProgress)
            bg to content to border
        }
    }

    GlassSurface(
        modifier = modifier
            .padding(KomorebiSpacing.XS)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {}, onRelease = { onClick() }, onCancel = {})
            }
            .combinedClickable(onClick = onClick, onLongClick = {}, role = androidx.compose.ui.semantics.Role.Button),
        variant = GlassVariant.Standard,
        shape = shapes.chip
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = KomorebiSpacing.Medium, vertical = KomorebiSpacing.Small)
                .graphicsLayer { scaleX = 1f - (1f - selectProgress) * 0.02f },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.invoke()
            if (leadingIcon != null) androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(KomorebiSpacing.XS))
            Text(
                text = label,
                style = typography.labelMedium,
                color = contentColor
            )
            if (trailingIcon != null) androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(KomorebiSpacing.XS))
            trailingIcon?.invoke()
        }
    }
}

enum class LiquidChipVariant {
    Standard, Tonal, Outline
}

// ========================================================================
// LIQUID PROGRESS - Organic, flowing progress indicator
// ========================================================================

@Composable
fun LiquidProgress(
    progress: Float, // 0f to 1f
    modifier: Modifier = Modifier,
    variant: LiquidProgressVariant = LiquidProgressVariant.Linear,
    trackColor: Color = komorebiColors().surfaceGlassSubtle,
    indicatorColor: Color = komorebiColors().primaryGlow,
    height: androidx.compose.ui.unit.Dp = 4.dp
) {
    val colors = komorebiColors()
    val shapes = komorebiShapes()

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = KomorebiMotion.TweenLiquid
    )

    when (variant) {
        LiquidProgressVariant.Linear -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(trackColor, shapes.pill)
            ) {
                val indicatorWidth = animatedProgress * (modifier.measuredWidth ?: 0)
                Box(
                    modifier = Modifier
                        .width(indicatorWidth.px)
                        .fillMaxHeight()
                        .background(
                            brush = ShaderBrush(
                                LinearGradient(
                                    colors = listOf(colors.primary, colors.primaryGlow, colors.secondaryLight),
                                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                    end = androidx.compose.ui.geometry.Offset(modifier.measuredWidth ?: 0, 0f),
                                    tileMode = TileMode.Mirror
                                )
                            ),
                            shape = shapes.pill
                        )
                )
            }
        }
        LiquidProgressVariant.Circular -> {
            // Circular implementation
            Box(
                modifier = modifier.size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.size(48.dp)) {
                    val strokeWidth = 4f
                    val radius = (size.minDimension - strokeWidth) / 2
                    val center = size.center

                    // Track
                    drawCircle(
                        color = trackColor,
                        center = center,
                        radius = radius,
                        style = androidx.compose.ui.draw.Stroke(width = strokeWidth)
                    )

                    // Progress with gradient sweep
                    val sweepAngle = animatedProgress * 360f
                    drawArc(
                        brush = ShaderBrush(
                            androidx.compose.ui.graphics.SweepGradient(
                                center = center,
                                colors = listOf(colors.primary, colors.primaryGlow, colors.secondaryLight, colors.primary),
                                startAngle = -90f,
                                endAngle = 270f
                            )
                        ),
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = androidx.compose.ui.draw.Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.Cap.Round),
                        center = center,
                        radius = radius
                    )
                }
            }
        }
    }
}

enum class LiquidProgressVariant {
    Linear, Circular
}

// ========================================================================
// SHIMMER EFFECT - Premium loading skeleton with liquid flow
// ========================================================================

@Composable
fun ShimmerSkeleton(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = komorebiShapes().small,
    baseColor: Color = komorebiColors().surfaceGlassSubtle,
    highlightColor: Color = komorebiColors().surfaceGlassHighlight.copy(alpha = 0.3f)
) {
    val animatedOffset by remember {
        mutableStateOf(-1.5f)
    }

    LaunchedEffect(Unit) {
        val infiniteAnimation = androidx.compose.animation.core.animate(
            initialValue = -1.5f,
            targetValue = 1.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1500, easing = KomorebiMotion.LiquidEaseInOut),
                repeatMode = androidx.compose.animation.core.RepeatMode.Restart
            )
        )
        // Note: In real implementation, use collectAsStateWithLifecycle
    }

    Box(
        modifier = modifier
            .background(baseColor, shape)
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val shimmerWidth = width * 0.4f
            val startX = (animatedOffset + 1.5f) / 3f * (width + shimmerWidth) - shimmerWidth

            drawRect(
                brush = ShaderBrush(
                    LinearGradient(
                        colors = listOf(
                            baseColor,
                            highlightColor,
                            baseColor
                        ),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(shimmerWidth, 0f),
                        tileMode = TileMode.Clamp
                    )
                ),
                topLeft = androidx.compose.ui.geometry.Offset(startX, 0f),
                size = androidx.compose.ui.geometry.Size(shimmerWidth, height)
            )
        }
    }
}

// ========================================================================
// GLASS NAVIGATION BAR - Floating liquid nav bar
// ========================================================================

@Composable
fun GlassNavigationBar(
    modifier: Modifier = Modifier,
    items: List<NavBarItem>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit
) {
    val colors = komorebiColors()
    val shapes = komorebiShapes()

    GlassSurface(
        modifier = modifier
            .fillMaxWidth()
            .padding(KomorebiSpacing.Medium)
            .height(72.dp),
        variant = GlassVariant.Strong,
        shape = shapes.pill
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                val selectProgress by animateFloatAsState(
                    targetValue = if (isSelected) 1f else 0f,
                    animationSpec = KomorebiMotion.SpringGentle
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(KomorebiSpacing.Small)
                        .pointerInput(Unit) {
                            detectTapGestures(onPress = {}, onRelease = { onItemClick(index) }, onCancel = {})
                        }
                        .combinedClickable(onClick = { onItemClick(index) }, onLongClick = {})
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer { scaleX = 0.9f + selectProgress * 0.1f; scaleY = 0.9f + selectProgress * 0.1f },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        androidx.compose.foundation.Canvas(modifier = Modifier.size(24.dp)) {
                            // Icon drawing would go here
                        }
                        Text(
                            text = item.label,
                            style = komorebiTypography().labelSmall.copy(
                                color = colors.contentTertiary.lerp(colors.primaryGlow, selectProgress)
                            )
                        )
                    }
                }
            }
        }
    }
}

data class NavBarItem(
    val icon: @Composable () -> Unit,
    val label: String,
    val route: String
)

// ========================================================================
// LIQUID SWITCH - Organic toggle with fluid thumb animation
// ========================================================================

@Composable
fun LiquidSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SwitchColors = SwitchColors.Default
) {
    val themeColors = komorebiColors()
    val shapes = komorebiShapes()

    val thumbPosition by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = KomorebiMotion.SpringWobbly
    )
    val trackColor by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = KomorebiMotion.TweenMedium
    )

    val trackWidth = 52.dp
    val trackHeight = 28.dp
    val thumbSize = 24.dp
    val thumbPadding = 2.dp

    val currentTrackColor = colors.trackOff.lerp(colors.trackOn, trackColor)
    val currentThumbColor = colors.thumbOff.lerp(colors.thumbOn, thumbPosition)

    Box(
        modifier = modifier
            .size(trackWidth, trackHeight)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {},
                    onRelease = { if (enabled) onCheckedChange(!checked) },
                    onCancel = {}
                )
            }
            .combinedClickable(
                onClick = { if (enabled) onCheckedChange(!checked) },
                onLongClick = {},
                enabled = enabled,
                role = androidx.compose.ui.semantics.Role.Switch
            )
            .background(currentTrackColor, shapes.pill)
    ) {
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset(x = (thumbPadding + (trackWidth - thumbSize - 2 * thumbPadding) * thumbPosition).dp)
                .graphicsLayer {
                    scaleX = 1f + (1f - thumbPosition) * 0.1f
                    scaleY = 1f + (1f - thumbPosition) * 0.1f
                }
                .background(currentThumbColor, shapes.pill)
                .shadow(GlassEffects.GlassElevation1, shapes.pill)
        )
    }
}

@Immutable
data class SwitchColors(
    val trackOn: Color,
    val trackOff: Color,
    val thumbOn: Color,
    val thumbOff: Color
) {
    companion object {
        val Default = SwitchColors(
            trackOn = komorebiColors().primary.copy(alpha = 0.5f),
            trackOff = komorebiColors().surfaceGlassSubtle,
            thumbOn = komorebiColors().primaryGlow,
            thumbOff = komorebiColors().contentPrimary
        )
    }
}

// ========================================================================
// LIQUID SLIDER - Organic slider with fluid thumb and track fill
// ========================================================================

@Composable
fun LiquidSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    range: androidx.compose.ui.unit.FloatRange = 0f..1f,
    steps: Int = 0,
    colors: SliderColors = SliderColors.Default
) {
    val themeColors = komorebiColors()
    val shapes = komorebiShapes()

    val trackHeight = 4.dp
    val thumbSize = 20.dp

    val normalizedValue = (value - range.start) / (range.endInclusive - range.start)
    val snappedValue = if (steps > 0) {
        val step = 1f / steps
        (normalizedValue / step).roundToInt() * step
    } else normalizedValue

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .pointerInput(Unit) {
                // Drag handling would go here
            }
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            // Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackHeight)
                    .background(colors.trackOff, shapes.pill)
            ) {
                // Active track fill
                Box(
                    modifier = Modifier
                        .width({ (it * snappedValue).dp })
                        .fillMaxHeight()
                        .background(
                            brush = ShaderBrush(
                                LinearGradient(
                                    colors = listOf(colors.activeStart, colors.activeEnd),
                                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                    end = androidx.compose.ui.geometry.Offset(1f, 0f)
                                )
                            ),
                            shape = shapes.pill
                        )
                )
            }
        }
    }
}

@Immutable
data class SliderColors(
    val trackOff: Color,
    val activeStart: Color,
    val activeEnd: Color,
    val thumb: Color
) {
    companion object {
        val Default = SliderColors(
            trackOff = komorebiColors().surfaceGlassSubtle,
            activeStart = komorebiColors().primary,
            activeEnd = komorebiColors().primaryGlow,
            thumb = komorebiColors().primaryGlow
        )
    }
}