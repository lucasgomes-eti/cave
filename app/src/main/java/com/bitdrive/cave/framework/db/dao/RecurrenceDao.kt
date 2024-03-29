package com.bitdrive.cave.framework.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import com.bitdrive.cave.framework.db.model.RecurrenceEntity

@Dao
interface RecurrenceDao {
    @Insert(onConflict = REPLACE)
    suspend fun add(recurrenceEntity: RecurrenceEntity): Long

    @Delete
    suspend fun delete(recurrenceEntity: RecurrenceEntity)
}