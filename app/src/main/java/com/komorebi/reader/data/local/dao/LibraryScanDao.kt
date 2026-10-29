package com.komorebi.reader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.komorebi.reader.data.local.entity.LibraryScanEntity
import com.komorebi.reader.data.local.entity.ScanStatus

@Dao
interface LibraryScanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scan: LibraryScanEntity): Long

    @Update
    suspend fun update(scan: LibraryScanEntity): Int

    @Query("SELECT * FROM library_scans WHERE id = :id")
    fun getById(id: Long): Flow<LibraryScanEntity?>

    @Query("SELECT * FROM library_scans ORDER BY startedAt DESC LIMIT 1")
    fun getLatestScan(): Flow<LibraryScanEntity?>

    @Query("SELECT * FROM library_scans WHERE status = :status")
    fun getByStatus(status: ScanStatus): Flow<List<LibraryScanEntity>>

    @Query("SELECT * FROM library_scans ORDER BY startedAt DESC LIMIT :limit")
    fun getRecentScans(limit: Int): Flow<List<LibraryScanEntity>>

    @Query("UPDATE library_scans SET status = :status, processedFiles = :processed, newBooks = :newBooks, updatedBooks = :updated, errors = :errors, errorLog = :errorLog WHERE id = :id")
    suspend fun updateProgress(
        id: Long,
        status: ScanStatus,
        processed: Int,
        newBooks: Int,
        updated: Int,
        errors: Int,
        errorLog: String
    ): Int

    @Query("UPDATE library_scans SET status = :status, completedAt = :now WHERE id = :id")
    suspend fun completeScan(id: Long, status: ScanStatus, now: String): Int
}