package com.bitdrive.cave.framework

import com.bitdrive.core.data.AlarmRepository
import com.bitdrive.core.interactors.AddAlarm
import com.bitdrive.core.interactors.GetAlarms
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
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
}