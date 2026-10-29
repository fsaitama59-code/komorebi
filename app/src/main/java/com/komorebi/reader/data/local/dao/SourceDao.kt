package com.komorebi.reader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.komorebi.reader.data.local.entity.SourceEntity

@Dao
interface SourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(source: SourceEntity): Long

    @Update
    suspend fun update(source: SourceEntity): Int

    @Query("SELECT * FROM sources WHERE id = :id")
    fun getById(id: String): Flow<SourceEntity?>

    @Query("SELECT * FROM sources WHERE enabled = 1 ORDER BY priority ASC")
    fun getEnabledSources(): Flow<List<SourceEntity>>

    @Query("SELECT * FROM sources ORDER BY priority ASC")
    fun getAllSources(): Flow<List<SourceEntity>>

    @Query("UPDATE sources SET enabled = :enabled WHERE id = :id")
    suspend fun setEnabled(id: String, enabled: Boolean): Int

    @Query("UPDATE sources SET priority = :priority WHERE id = :id")
    suspend fun setPriority(id: String, priority: Int): Int

    @Query("UPDATE sources SET authToken = :token WHERE id = :id")
    suspend fun setAuthToken(id: String, token: String?): Int

    @Query("UPDATE sources SET lastSyncAt = :now, lastError = :error WHERE id = :id")
    suspend fun updateSyncStatus(id: String, now: String, error: String?): Int
}