package com.bitdrive.core.data

import com.bitdrive.core.domain.Alarm

class AlarmRepository(private val alarmDataSource: AlarmDataSource) {
    suspend fun addAlarm(alarm: Alarm) = alarmDataSource.add(alarm)
    suspend fun getAlarms() = alarmDataSource.read()
    suspend fun removeAlarm(alarm: Alarm) = alarmDataSource.remove(alarm)
}