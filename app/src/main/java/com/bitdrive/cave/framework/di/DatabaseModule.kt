package com.bitdrive.cave.framework.di

import android.app.Application
import androidx.room.Room
import com.bitdrive.cave.framework.db.Database
import com.bitdrive.cave.framework.db.dao.AlarmDao
import com.bitdrive.cave.framework.db.dao.RecurrenceDao

class DatabaseModule {

    fun provideDatabase(app: Application): Database {
        return Room
            .databaseBuilder(app, Database::class.java, "cave.db")
            .build()
    }

    internal fun provideAlarmDao(database: Database): AlarmDao {
        return database.alarmDao()
    }

    internal fun provideRecurrenceDao(database: Database): RecurrenceDao {
        return database.recurrenceDao()
    }
}