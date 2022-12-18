package com.bitdrive.core.data

import com.bitdrive.core.domain.Alarm

class AlarmRepository(private val alarmDataSource: AlarmDataSource) {
    suspend fun addAlarm(alarm: Alarm) = alarmDataSource.add(alarm)
    fun getAlarms() = alarmDataSource.read()
    suspend fun getAlarmById(alarmId: Long) = alarmDataSource.readById(alarmId)
    suspend fun updateAlarm(alarm: Alarm) = alarmDataSource.update(alarm)
    suspend fun removeAlarm(alarm: Alarm) = alarmDataSource.remove(alarm)
}