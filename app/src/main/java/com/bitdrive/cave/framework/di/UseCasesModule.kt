package com.bitdrive.cave.framework.di

import com.bitdrive.core.data.AlarmRepository
import com.bitdrive.core.interactors.AddAlarm
import com.bitdrive.core.interactors.DeleteAlarm
import com.bitdrive.core.interactors.GetAlarms
import com.bitdrive.core.interactors.UpdateAlarm
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
    fun provideAddAlarm(alarmRepository: AlarmRepository) = AddAlarm(alarmRepository)

    @Singleton
    @Provides
    fun provideUpdateAlarm(alarmRepository: AlarmRepository) = UpdateAlarm(alarmRepository)

    @Singleton
    @Provides
    fun provideDeleteAlarm(alarmRepository: AlarmRepository) = DeleteAlarm(alarmRepository)
}