package com.bitdrive.cave.framework.di

import android.app.AlarmManager
import android.content.Context
import android.content.Context.ALARM_SERVICE

class AppModule {
    fun provideAlarmManager(appContext: Context): AlarmManager =
        appContext.getSystemService(ALARM_SERVICE) as AlarmManager
}