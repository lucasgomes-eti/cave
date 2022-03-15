package com.bitdrive.cave.framework.di

import com.bitdrive.core.data.AlarmDataSource
import com.bitdrive.core.data.AlarmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideAlarmRepository(alarmDataSource: AlarmDataSource): AlarmRepository {
        return AlarmRepository(alarmDataSource)
    }
}