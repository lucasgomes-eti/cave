package com.bitdrive.cave.framework

import com.bitdrive.core.data.AlarmDataSource
import com.bitdrive.core.data.AlarmRepository
import com.bitdrive.core.interactors.GetAlarms
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

//    @Singleton
//    @Binds
//    abstract fun alarmDataSource(
//        inMemoryAlarmDataSource: InMemoryAlarmDataSource
//    ): AlarmDataSource


    @Singleton
    @Provides
    fun provideAlarmRepository(): AlarmRepository {
        return AlarmRepository(InMemoryAlarmDataSource())
    }
}