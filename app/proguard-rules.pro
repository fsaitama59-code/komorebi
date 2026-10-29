# ========================================================================
# Komorebi ProGuard/R8 Rules
# ========================================================================

# Keep the Application class
-keep class com.komorebi.reader.KomorebiApplication { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class com.komorebi.reader.Hilt_* { *; }

# Keep Room entities and DAOs
-keep class com.komorebi.reader.data.local.entity.** { *; }
-keep class com.komorebi.reader.data.local.dao.** { *; }
-keep class com.komorebi.reader.data.local.database.** { *; }
-keep class com.komorebi.reader.data.local.converters.** { *; }

# Keep Repository and Preferences
-keep class com.komorebi.reader.data.repository.** { *; }
-keep class com.komorebi.reader.data.preferences.** { *; }

# Keep ViewModels
-keep class com.komorebi.reader.ui.screens.**ViewModel { *; }

# Keep UI components
-keep class com.komorebi.reader.ui.components.** { *; }
-keep class com.komorebi.reader.ui.theme.** { *; }
-keep class com.komorebi.reader.ui.navigation.** { *; }
-keep class com.komorebi.reader.ui.screens.** { *; }

# Keep DI modules
-keep class com.komorebi.reader.di.** { *; }

# Retrofit/OkHttp
-keep class okhttp3.** { *; }
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-dontwarn okhttp3.**
-dontwarn retrofit2.**

# Kotlinx Serialization
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}

# Kotlinx Coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Coil
-keep class coil3.** { *; }
-dontwarn coil3.**

# Room
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# Hilt
-keep class dagger.hilt.android.** { *; }

# Material3
-keep class androidx.compose.material3.** { *; }

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable { *; }

# Keep Enum values
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep annotations
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Optimize
-optimizations !code/simplification/arithmetic
-allowaccessmodification

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
}

# Keep line numbers for crash reporting
-keepattributes SourceFile,LineNumberTable

# ========================================================================
# Specific library rules
# ========================================================================

# OkHttp
-dontwarn okhttp3.internal.platform.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Retrofit
-dontwarn retrofit2.Platform$Java8

# Kotlin
-dontwarn kotlin.coroutines.jvm.internal.**
-dontwarn kotlinx.coroutines.internal.**

# Room KSP generated
-keep class com.komorebi.reader.data.local.database.KomorebiDatabase_Impl { *; }

# Serialization
-keep class kotlinx.serialization.descriptors.** { *; }

# Prevent obfuscation of public API
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}