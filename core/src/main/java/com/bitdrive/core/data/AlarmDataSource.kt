package com.bitdrive.core.data

import com.bitdrive.core.domain.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmDataSource {
    suspend fun add(alarm: Alarm)
    fun read(): Flow<List<Alarm>>
    suspend fun update(alarm: Alarm)
    suspend fun remove(alarm: Alarm)
}