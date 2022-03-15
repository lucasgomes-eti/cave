package com.bitdrive.cave.framework.di

import android.app.Application
import androidx.room.Room
import com.bitdrive.cave.framework.db.Database
import com.bitdrive.cave.framework.db.dao.AlarmDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(app: Application): Database {
        return Room
            .databaseBuilder(app, Database::class.java, "cave.db")
            .build()
    }

    @Singleton
    @Provides
    internal fun provideAlarmDao(database: Database): AlarmDao {
        return database.alarmDao()
    }
}