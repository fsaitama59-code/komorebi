package com.komorebi.reader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import com.komorebi.reader.data.local.entity.BookEntity
import com.komorebi.reader.data.local.entity.ChapterEntity
import com.komorebi.reader.data.local.entity.BookType
import com.komorebi.reader.data.local.entity.BookStatus

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: BookEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(books: List<BookEntity>): List<Long>

    @Update
    suspend fun update(book: BookEntity): Int

    @Delete
    suspend fun delete(book: BookEntity): Int

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("SELECT * FROM books WHERE id = :id")
    fun getById(id: Long): Flow<BookEntity?>

    @Query("SELECT * FROM books WHERE sourceId = :sourceId AND sourceKey = :sourceKey")
    fun getBySourceKey(sourceId: String, sourceKey: String): Flow<BookEntity?>

    @Query("SELECT * FROM books WHERE sourceId = :sourceId")
    fun getBySource(sourceId: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE type = :type")
    fun getByType(type: BookType): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE isDownloaded = 1")
    fun getDownloaded(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books ORDER BY lastReadAt DESC NULLS LAST LIMIT :limit")
    fun getRecentlyRead(limit: Int): Flow<List<BookEntity>>

    @Query("SELECT * FROM books ORDER BY addedAt DESC LIMIT :limit")
    fun getRecentlyAdded(limit: Int): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE title LIKE :query OR author LIKE :query ORDER BY title")
    fun search(query: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books ORDER BY
        CASE WHEN :sortOrder = 'title' THEN title END ASC,
        CASE WHEN :sortOrder = 'author' THEN author END ASC,
        CASE WHEN :sortOrder = 'recentlyRead' THEN lastReadAt END DESC,
        CASE WHEN :sortOrder = 'progress' THEN readingProgress END DESC,
        CASE WHEN :sortOrder = 'addedDate' THEN addedAt END DESC,
        CASE WHEN :sortOrder = 'updatedDate' THEN updatedAt END DESC
    ")
    fun getAllSorted(sortOrder: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books")
    fun getAll(): Flow<List<BookEntity>>

    @Query("SELECT COUNT(*) FROM books")
    fun count(): Flow<Int>

    @Query("SELECT COUNT(*) FROM books WHERE isDownloaded = 1")
    fun countDownloaded(): Flow<Int>

    @Transaction
    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookWithChapters(bookId: Long): Flow<BookWithChapters?>

    @Query("UPDATE books SET lastReadAt = :now, readingProgress = :progress, lastChapterRead = :chapterId, lastPageRead = :page WHERE id = :id")
    suspend fun updateReadingProgress(id: Long, progress: Float, chapterId: Long, page: Int, now: String): Int

    @Query("UPDATE books SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Long, favorite: Boolean): Int

    @Query("UPDATE books SET isDownloaded = :downloaded, downloadPath = :path WHERE id = :id")
    suspend fun setDownloaded(id: Long, downloaded: Boolean, path: String?): Int

    @Query("DELETE FROM books WHERE sourceId = :sourceId")
    suspend fun deleteBySource(sourceId: String): Int
}

data class BookWithChapters(
    val book: BookEntity,
    val chapters: List<ChapterEntity>
)