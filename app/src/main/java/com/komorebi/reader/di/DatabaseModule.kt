package com.komorebi.reader.di

import android.content.Context
import androidx.room.Room
import com.komorebi.reader.data.local.database.KomorebiDatabase
import com.komorebi.reader.data.local.dao.BookDao
import com.komorebi.reader.data.local.dao.ChapterDao
import com.komorebi.reader.data.local.dao.ReadingProgressDao
import com.komorebi.reader.data.local.dao.SourceDao
import com.komorebi.reader.data.local.dao.BookmarkDao
import com.komorebi.reader.data.local.dao.LibraryScanDao
import com.komorebi.reader.data.preferences.PreferencesRepository
import com.komorebi.reader.data.repository.KomorebiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext context: Context): KomorebiDatabase {
        return Room.databaseBuilder(
            context,
            KomorebiDatabase::class.java,
            "komorebi_database"
        ).build()
    }

    @Provides
    fun provideBookDao(database: KomorebiDatabase): BookDao = database.bookDao()

    @Provides
    fun provideChapterDao(database: KomorebiDatabase): ChapterDao = database.chapterDao()

    @Provides
    fun provideProgressDao(database: KomorebiDatabase): ReadingProgressDao = database.readingProgressDao()

    @Provides
    fun provideSourceDao(database: KomorebiDatabase): SourceDao = database.sourceDao()

    @Provides
    fun provideBookmarkDao(database: KomorebiDatabase): BookmarkDao = database.bookmarkDao()

    @Provides
    fun provideScanDao(database: KomorebiDatabase): LibraryScanDao = database.libraryScanDao()

    @Provides
    @Singleton
    fun providePreferencesRepository(@dagger.hilt.android.qualifiers.ApplicationContext context: Context): PreferencesRepository {
        return PreferencesRepository(context)
    }

    @Provides
    @Singleton
    fun provideRepository(
        bookDao: BookDao,
        chapterDao: ChapterDao,
        progressDao: ReadingProgressDao,
        sourceDao: SourceDao,
        bookmarkDao: BookmarkDao,
        scanDao: LibraryScanDao,
        prefs: PreferencesRepository
    ): KomorebiRepository {
        return KomorebiRepository(
            bookDao, chapterDao, progressDao, sourceDao, bookmarkDao, scanDao, prefs
        )
    }
}