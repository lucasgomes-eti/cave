package com.bitdrive.cave.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bitdrive.cave.framework.db.dao.AlarmDao
import com.bitdrive.cave.framework.db.dao.RecurrenceDao
import com.bitdrive.cave.framework.db.model.AlarmEntity
import com.bitdrive.cave.framework.db.model.RecurrenceEntity

@Database(
    entities = [AlarmEntity::class, RecurrenceEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun recurrenceDao(): RecurrenceDao
}