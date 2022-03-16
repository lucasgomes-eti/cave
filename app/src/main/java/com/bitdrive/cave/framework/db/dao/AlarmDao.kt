package com.bitdrive.cave.framework.db.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.bitdrive.cave.framework.db.model.AlarmEntity
import com.bitdrive.cave.framework.db.vo.AlarmVO
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Transaction
    @Query("SELECT * FROM alarm")
    fun read(): Flow<List<AlarmVO>>

    @Insert(onConflict = REPLACE)
    suspend fun add(alarmEntity: AlarmEntity)

    @Delete
    suspend fun delete(alarmEntity: AlarmEntity)
}