package com.bitdrive.cave.framework.di

import com.bitdrive.cave.framework.datasource.AlarmDatabase
import com.bitdrive.core.data.AlarmDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModuleBinder {
    @Singleton
    @Binds
    abstract fun bindsAlarmDataSource(
        alarmDatabase: AlarmDatabase
    ): AlarmDataSource
}