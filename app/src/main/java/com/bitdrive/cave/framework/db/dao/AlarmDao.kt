package com.bitdrive.cave.framework.db.dao

import androidx.room.*
import com.bitdrive.cave.framework.db.model.AlarmEntity
import com.bitdrive.cave.framework.db.vo.AlarmVO
import com.bitdrive.core.data.AlarmDataSource
import com.bitdrive.core.domain.Alarm

@Dao
interface AlarmDao : AlarmDataSource {

    @Transaction
    @Query("SELECT * FROM alarm")
    suspend fun readAlarms(): List<AlarmVO>

    @Insert
    suspend fun addAlarm(alarmEntity: AlarmEntity)

    @Delete
    suspend fun deleteAlarm(alarmEntity: AlarmEntity)

    override suspend fun read(): List<Alarm> {
        return readAlarms().map { it.map() }
    }

    override suspend fun add(alarm: Alarm) {
        addAlarm(AlarmEntity(alarm))
    }

    override suspend fun remove(alarm: Alarm) {
        deleteAlarm(AlarmEntity(alarm))
    }
}