package com.komorebi.reader.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.komorebi.reader.data.local.converters.Converters
import com.komorebi.reader.data.local.entity.BookEntity
import com.komorebi.reader.data.local.entity.ChapterEntity
import com.komorebi.reader.data.local.entity.ReadingProgressEntity
import com.komorebi.reader.data.local.entity.SourceEntity
import com.komorebi.reader.data.local.entity.BookmarkEntity
import com.komorebi.reader.data.local.entity.LibraryScanEntity
import com.komorebi.reader.data.local.dao.BookDao
import com.komorebi.reader.data.local.dao.ChapterDao
import com.komorebi.reader.data.local.dao.ReadingProgressDao
import com.komorebi.reader.data.local.dao.SourceDao
import com.komorebi.reader.data.local.dao.BookmarkDao
import com.komorebi.reader.data.local.dao.LibraryScanDao

@Database(
    entities = [
        BookEntity::class,
        ChapterEntity::class,
        ReadingProgressEntity::class,
        SourceEntity::class,
        BookmarkEntity::class,
        LibraryScanEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class KomorebiDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun readingProgressDao(): ReadingProgressDao
    abstract fun sourceDao(): SourceDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun libraryScanDao(): LibraryScanDao
}