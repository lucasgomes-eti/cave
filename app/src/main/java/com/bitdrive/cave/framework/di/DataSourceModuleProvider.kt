package com.bitdrive.cave.framework.di

import com.bitdrive.cave.framework.datasource.AlarmDatabase
import com.bitdrive.cave.framework.datasource.AlarmInMemory
import com.bitdrive.cave.framework.db.dao.AlarmDao
import com.bitdrive.cave.framework.db.dao.RecurrenceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModuleProvider {
    @Singleton
    @Provides
    fun provideAlarmInMemory(): AlarmInMemory {
        return AlarmInMemory()
    }

    @Singleton
    @Provides
    fun provideAlarmDatabase(alarmDao: AlarmDao, recurrenceDao: RecurrenceDao): AlarmDatabase {
        return AlarmDatabase(alarmDao, recurrenceDao)
    }
}