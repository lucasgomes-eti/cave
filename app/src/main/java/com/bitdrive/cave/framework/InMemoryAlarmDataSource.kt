package com.bitdrive.cave.framework

import com.bitdrive.core.data.AlarmDataSource
import com.bitdrive.core.domain.Alarm

class InMemoryAlarmDataSource : AlarmDataSource {

    private val alarms = mutableListOf(Alarm(), Alarm(), Alarm())

    override suspend fun add(alarm: Alarm) {
        alarms.add(alarm)
    }

    override suspend fun read(): List<Alarm> = alarms

    override suspend fun remove(alarm: Alarm) {
        alarms.remove(alarm)
    }
}