package com.bitdrive.cave

import android.app.AlarmManager
import android.app.Application
import androidx.room.Room
import com.bitdrive.cave.framework.datasource.AlarmDatabase
import com.bitdrive.cave.framework.datasource.AlarmInMemory
import com.bitdrive.cave.framework.db.Database
import com.bitdrive.cave.ui.viewmodel.AlarmsViewModel
import com.bitdrive.cave.ui.viewmodel.NewOrEditAlarmViewModel
import com.bitdrive.core.data.AlarmDataSource
import com.bitdrive.core.data.AlarmRepository
import com.bitdrive.core.interactors.AddAlarm
import com.bitdrive.core.interactors.DeleteAlarm
import com.bitdrive.core.interactors.GetAlarmById
import com.bitdrive.core.interactors.GetAlarms
import com.bitdrive.core.interactors.ToggleAlarm
import com.bitdrive.core.interactors.UpdateAlarm
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    private val appModule = module {
        single { androidContext().getSystemService(ALARM_SERVICE) as AlarmManager }
        viewModelOf(::NewOrEditAlarmViewModel)
        viewModelOf(::AlarmsViewModel)
    }

    private val databaseModule = module {
        single {
            Room
                .databaseBuilder(androidContext(), Database::class.java, "cave.db")
                .build()
        }
        single { get<Database>().alarmDao() }
        single { get<Database>().recurrenceDao() }
    }

    private val dataSourceModule = module {
        single { AlarmInMemory() }
        single { AlarmDatabase(get(), get()) as AlarmDataSource }
    }

    private val repositoryModule = module {
        single { AlarmRepository(get()) }
    }

    private val useCasesModule = module {
        single { GetAlarms(get()) }
        single { GetAlarmById(get()) }
        single { AddAlarm(get()) }
        single { UpdateAlarm(get()) }
        single { DeleteAlarm(get()) }
        single { ToggleAlarm(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule, databaseModule, dataSourceModule, repositoryModule, useCasesModule)
        }
    }
}