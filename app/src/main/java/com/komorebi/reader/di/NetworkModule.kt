package com.komorebi.reader.di

import com.google.common.net.HttpHeaders
import com.jakewharton.retrofit2.adapter.kotlinx.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import java.io.File
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@dagger.hilt.android.qualifiers.ApplicationContext context: android.content.Context): OkHttpClient {
        val cacheDir = File(context.cacheDir, "http_cache")
        val cache = Cache(cacheDir, 50 * 1024 * 1024) // 50 MB

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Use a constant instead of BuildConfig.DEBUG
            // BuildConfig will be available after first successful build
            level = HttpLoggingInterceptor.Level.BODY
        }

        val userAgentInterceptor = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header(HttpHeaders.USER_AGENT, "Komorebi/1.0.0 (Android; +https://github.com/komorebi)")
                .build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(userAgentInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            allowStructuredMapKeys = true
        }

        return Retrofit.Builder()
            .baseUrl("https://api.mangadex.org/")
            .client(client)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(asConverterFactory(json))
            .build()
    }
}