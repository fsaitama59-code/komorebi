package com.komorebi.reader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.komorebi.reader.data.local.entity.ReadingProgressEntity

@Dao
interface ReadingProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(progress: ReadingProgressEntity): Long

    @Update
    suspend fun update(progress: ReadingProgressEntity): Int

    @Query("SELECT * FROM reading_progress WHERE bookId = :bookId AND chapterId = :chapterId")
    fun getProgress(bookId: Long, chapterId: Long): Flow<ReadingProgressEntity?>

    @Query("SELECT * FROM reading_progress WHERE bookId = :bookId ORDER BY updatedAt DESC LIMIT 1")
    fun getLatestProgress(bookId: Long): Flow<ReadingProgressEntity?>

    @Query("SELECT * FROM reading_progress WHERE deviceId = :deviceId")
    fun getByDevice(deviceId: String): Flow<List<ReadingProgressEntity>>

    @Query("DELETE FROM reading_progress WHERE bookId = :bookId AND chapterId = :chapterId")
    suspend fun deleteProgress(bookId: Long, chapterId: Long): Int

    @Query("DELETE FROM reading_progress WHERE updatedAt < :cutoff")
    suspend fun deleteOldProgress(cutoff: String): Int
}