package com.bitdrive.cave.framework.di

import com.bitdrive.cave.framework.datasource.AlarmDatabase
import com.bitdrive.core.data.AlarmDataSource

abstract class DataSourceModuleBinder {
    abstract fun bindsAlarmDataSource(
        alarmDatabase: AlarmDatabase
    ): AlarmDataSource
}