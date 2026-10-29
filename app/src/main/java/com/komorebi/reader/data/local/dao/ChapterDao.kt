package com.komorebi.reader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import com.komorebi.reader.data.local.entity.ChapterEntity

@Dao
interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chapter: ChapterEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chapters: List<ChapterEntity>): List<Long>

    @Update
    suspend fun update(chapter: ChapterEntity): Int

    @Delete
    suspend fun delete(chapter: ChapterEntity): Int

    @Query("DELETE FROM chapters WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("SELECT * FROM chapters WHERE id = :id")
    fun getById(id: Long): Flow<ChapterEntity?>

    @Query("SELECT * FROM chapters WHERE bookId = :bookId ORDER BY number ASC")
    fun getByBookId(bookId: Long): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE bookId = :bookId AND isRead = 0 ORDER BY number ASC LIMIT 1")
    fun getFirstUnread(bookId: Long): Flow<ChapterEntity?>

    @Query("SELECT * FROM chapters WHERE bookId = :bookId AND number > :currentNumber ORDER BY number ASC LIMIT 1")
    fun getNextChapter(bookId: Long, currentNumber: Double): Flow<ChapterEntity?>

    @Query("SELECT * FROM chapters WHERE bookId = :bookId AND number < :currentNumber ORDER BY number DESC LIMIT 1")
    fun getPreviousChapter(bookId: Long, currentNumber: Double): Flow<ChapterEntity?>

    @Query("SELECT * FROM chapters WHERE bookId = :bookId AND sourceChapterId = :sourceId")
    fun getBySourceId(bookId: Long, sourceId: String): Flow<ChapterEntity?>

    @Query("SELECT COUNT(*) FROM chapters WHERE bookId = :bookId")
    fun countByBook(bookId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM chapters WHERE bookId = :bookId AND isRead = 1")
    fun countReadByBook(bookId: Long): Flow<Int>

    @Query("UPDATE chapters SET isRead = 1, readAt = :now WHERE id = :id")
    suspend fun markAsRead(id: Long, now: String): Int

    @Query("UPDATE chapters SET localPath = :path, downloadedAt = :now WHERE id = :id")
    suspend fun setDownloaded(id: Long, path: String, now: String): Int

    @Query("DELETE FROM chapters WHERE bookId = :bookId")
    suspend fun deleteByBookId(bookId: Long): Int
}