package com.bitdrive.cave.framework.di

import com.bitdrive.core.data.AlarmRepository
import com.bitdrive.core.interactors.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCasesModule {
    @Singleton
    @Provides
    fun provideGetAlarms(alarmRepository: AlarmRepository) = GetAlarms(alarmRepository)

    @Singleton
    @Provides
    fun provideGetAlarmById(alarmRepository: AlarmRepository) = GetAlarmById(alarmRepository)

    @Singleton
    @Provides
    fun provideAddAlarm(alarmRepository: AlarmRepository) = AddAlarm(alarmRepository)

    @Singleton
    @Provides
    fun provideUpdateAlarm(alarmRepository: AlarmRepository) = UpdateAlarm(alarmRepository)

    @Singleton
    @Provides
    fun provideDeleteAlarm(alarmRepository: AlarmRepository) = DeleteAlarm(alarmRepository)

    @Singleton
    @Provides
    fun provideToogleAlarm(alarmRepository: AlarmRepository) = ToggleAlarm(alarmRepository)
}