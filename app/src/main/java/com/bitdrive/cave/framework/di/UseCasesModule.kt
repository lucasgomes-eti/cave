package com.bitdrive.cave.framework.di

import com.bitdrive.core.data.AlarmRepository
import com.bitdrive.core.interactors.*

class UseCasesModule {
    fun provideGetAlarms(alarmRepository: AlarmRepository) = GetAlarms(alarmRepository)

    fun provideGetAlarmById(alarmRepository: AlarmRepository) = GetAlarmById(alarmRepository)

    fun provideAddAlarm(alarmRepository: AlarmRepository) = AddAlarm(alarmRepository)

    fun provideUpdateAlarm(alarmRepository: AlarmRepository) = UpdateAlarm(alarmRepository)

    fun provideDeleteAlarm(alarmRepository: AlarmRepository) = DeleteAlarm(alarmRepository)

    fun provideToogleAlarm(alarmRepository: AlarmRepository) = ToggleAlarm(alarmRepository)
}