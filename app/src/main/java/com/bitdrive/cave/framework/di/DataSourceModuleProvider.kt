package com.bitdrive.cave.framework.di

import com.bitdrive.cave.framework.InMemoryAlarmDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModuleProvider {
    @Singleton
    @Provides
    fun provideInMemoryAlarmDataSource(): InMemoryAlarmDataSource {
        return InMemoryAlarmDataSource()
    }
}