package com.komorebi.reader.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Extension functions for common operations
 */

// ===== CONTEXT EXTENSIONS =====

fun Context.getColorCompat(@androidx.annotation.ColorRes resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

fun Context.getDrawableCompat(@androidx.annotation.DrawableRes resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
}

fun Context.dpToPx(dp: Float): Float {
    return dp * resources.displayMetrics.density
}

fun Context.pxToDp(px: Float): Float {
    return px / resources.displayMetrics.density
}

fun Context.getStringSafe(@androidx.annotation.StringRes resId: Int, vararg args: Any): String {
    return getString(resId, *args)
}

// ===== STRING EXTENSIONS =====

fun String.toAnnotatedString(
    color: Color = Color.Unspecified,
    style: SpanStyle? = null
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(color = color, fontWeight = style?.fontWeight)) {
            append(this@toAnnotatedString)
        }
    }
}

fun String.highlight(query: String, highlightColor: Int): SpannableString {
    val spannable = SpannableString(this)
    val lower = this.lowercase()
    val queryLower = query.lowercase()
    var index = lower.indexOf(queryLower)
    while (index >= 0) {
        spannable.setSpan(
            ForegroundColorSpan(highlightColor),
            index,
            index + query.length,
            0
        )
        index = lower.indexOf(queryLower, index + query.length)
    }
    return spannable
}

fun String.isNullOrBlank(): Boolean = this.isNullOrEmpty() || this.trim().isEmpty()

// ===== DRAWABLE EXTENSIONS =====

fun Drawable.toBitmap(): Bitmap {
    return if (this is BitmapDrawable) {
        this.bitmap
    } else {
        val bitmap = Bitmap.createBitmap(
            intrinsicWidth.coerceAtLeast(1),
            intrinsicHeight.coerceAtLeast(1),
            Bitmap.Config.ARGB_8888
        )
        Canvas(bitmap).apply { draw(this@toBitmap) }
        bitmap
    }
}

fun Drawable.tint(color: Int): Drawable {
    val wrapped = mutate()
    wrapped.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN))
    return wrapped
}

fun Drawable.tintList(colors: android.content.res.ColorStateList): Drawable {
    val wrapped = mutate()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        wrapped.setTintList(colors)
    }
    return wrapped
}

// ===== URI EXTENSIONS =====

fun Uri.getFileName(context: Context): String = FileUtils.getFileName(context, this)

fun Uri.getFileExtension(): String = FileUtils.getFileExtension(this)

fun Uri.isSupportedBookFile(): Boolean = FileUtils.isSupportedBookFile(this)

// ===== COROUTINE EXTENSIONS =====

fun CoroutineScope.launchAndForget(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(SupervisorJob() + coroutineContext) { block() }
}

suspend fun <T> withTimeoutOrDefault(
    timeMillis: Long,
    defaultValue: T,
    block: suspend () -> T
): T {
    return try {
        withTimeout(timeMillis) { block() }
    } catch (e: Exception) {
        defaultValue
    }
}

// ===== NUMBER EXTENSIONS =====

fun Int.toDurationString(): String {
    return when {
        this < 60 -> "${this}s"
        this < 3600 -> "${this / 60}m ${this % 60}s"
        this < 86400 -> "${this / 3600}h ${(this % 3600) / 60}m"
        else -> "${this / 86400}j ${(this % 86400) / 3600}h"
    }
}

fun Long.toFileSizeString(): String = FileUtils.formatFileSize(this)

fun Float.toPercentString(decimals: Int = 0): String = NumberUtils.formatPercent(this, decimals)

// ===== COLOR EXTENSIONS =====

fun Color.toArgb(): Int {
    return (alpha * 255).toInt() shl 24 or
           (red * 255).toInt() shl 16 or
           (green * 255).toInt() shl 8 or
           (blue * 255).toInt()
}

fun Color.withAlpha(alpha: Float): Color {
    return copy(alpha = alpha.coerceIn(0f, 1f))
}

fun Int.toComposeColor(): Color = Color(this)

// ===== LIST EXTENSIONS =====

fun <T> List<T>.chunkedBySize(size: Int): List<List<T>> {
    return chunked(size)
}

fun <T> List<T>.distinctBy(selector: (T) -> Any): List<T> {
    val seen = mutableSetOf<Any>()
    return filter { seen.add(selector(it)) }
}

// ===== BOOLEAN EXTENSIONS =====

infix fun Boolean.orElse(other: Boolean): Boolean = this || other
infix fun Boolean.andAlso(other: Boolean): Boolean = this && other

// ===== NULLABLE EXTENSIONS =====

fun <T> T?.letNotNull(block: (T) -> Unit): T? {
    this?.let(block)
    return this
}

fun <T, R> T?.mapNotNull(block: (T) -> R?): R? {
    return this?.let(block)
}

// ===== TIME EXTENSIONS =====

val Long.hours: Long get() = this * 60 * 60 * 1000
val Long.minutes: Long get() = this * 60 * 1000
val Long.seconds: Long get() = this * 1000

suspend fun delayMillis(millis: Long) = kotlinx.coroutines.delay(millis)