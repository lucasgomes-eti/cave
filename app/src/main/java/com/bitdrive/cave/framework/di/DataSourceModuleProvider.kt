package com.bitdrive.cave.framework.di

import com.bitdrive.cave.framework.datasource.AlarmDatabase
import com.bitdrive.cave.framework.datasource.AlarmInMemory
import com.bitdrive.cave.framework.db.dao.AlarmDao
import com.bitdrive.cave.framework.db.dao.RecurrenceDao

class DataSourceModuleProvider {

    fun provideAlarmInMemory(): AlarmInMemory {
        return AlarmInMemory()
    }

    fun provideAlarmDatabase(alarmDao: AlarmDao, recurrenceDao: RecurrenceDao): AlarmDatabase {
        return AlarmDatabase(alarmDao, recurrenceDao)
    }
}