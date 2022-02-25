package com.bitdrive.cave

import android.app.Application
import com.bitdrive.cave.framework.AppViewModelFactory
import com.bitdrive.cave.framework.InMemoryAlarmDataSource
import com.bitdrive.cave.framework.Interactors
import com.bitdrive.core.data.AlarmRepository
import com.bitdrive.core.interactors.AddAlarm
import com.bitdrive.core.interactors.GetAlarms

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val alarmRepository = AlarmRepository(InMemoryAlarmDataSource())

        AppViewModelFactory.inject(
            this,
            Interactors(AddAlarm(alarmRepository), GetAlarms(alarmRepository))
        )
    }
}