package com.bitdrive.cave.framework.datasource

import com.bitdrive.core.data.AlarmDataSource
import com.bitdrive.core.domain.Alarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AlarmInMemory : AlarmDataSource {

    private val alarms = mutableListOf(Alarm(), Alarm(), Alarm())

    override suspend fun add(alarm: Alarm) {
        alarms.add(alarm)
    }

    override fun read(): Flow<List<Alarm>> = flowOf(alarms)

    override suspend fun readById(alarmId: Long) = alarms.first()

    override suspend fun update(alarm: Alarm) {
        alarms.replaceAll{ alarm }
    }

    override suspend fun remove(alarm: Alarm) {
        alarms.remove(alarm)
    }
}