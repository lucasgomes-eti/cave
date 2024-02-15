package com.bitdrive.cave.framework.di

import com.bitdrive.core.data.AlarmDataSource
import com.bitdrive.core.data.AlarmRepository

class RepositoryModule {
    fun provideAlarmRepository(alarmDataSource: AlarmDataSource): AlarmRepository {
        return AlarmRepository(alarmDataSource)
    }
}