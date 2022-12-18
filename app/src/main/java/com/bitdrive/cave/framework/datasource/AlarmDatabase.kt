package com.bitdrive.cave.framework.datasource

import com.bitdrive.cave.framework.db.dao.AlarmDao
import com.bitdrive.cave.framework.db.dao.RecurrenceDao
import com.bitdrive.cave.framework.db.model.AlarmEntity
import com.bitdrive.cave.framework.db.model.RecurrenceEntity
import com.bitdrive.core.data.AlarmDataSource
import com.bitdrive.core.domain.Alarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AlarmDatabase(
    private val alarmDao: AlarmDao,
    private val recurrenceDao: RecurrenceDao
) : AlarmDataSource {
    override suspend fun add(alarm: Alarm) {
        val rowId = alarm.repeat?.let {
            recurrenceDao.add(RecurrenceEntity(it))
        }
        alarmDao.add(AlarmEntity(alarm = alarm, recurrenceId = rowId))
    }

    override fun read(): Flow<List<Alarm>> {
        return alarmDao.read().map { it.map { vo -> vo.map() } }
    }

    override suspend fun readById(alarmId: Long): Alarm {
        return alarmDao.readById(alarmId).map()
    }

    override suspend fun update(alarm: Alarm) {
        add(alarm)
    }

    override suspend fun remove(alarm: Alarm) {
        alarmDao.delete(AlarmEntity(alarm))
        alarm.repeat?.let { recurrenceDao.delete(RecurrenceEntity(it)) }
    }
}