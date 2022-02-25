package com.bitdrive.core.data

import com.bitdrive.core.domain.Alarm

interface AlarmDataSource {
    suspend fun add(alarm: Alarm)
    suspend fun read(): List<Alarm>
    suspend fun remove(alarm: Alarm)
}