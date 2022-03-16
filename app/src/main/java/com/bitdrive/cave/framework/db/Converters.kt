package com.bitdrive.cave.framework.db

import androidx.room.TypeConverter
import com.bitdrive.core.domain.Recurrence
import kotlinx.datetime.*

class Converters {
    @TypeConverter
    fun fromLocalDateTimeToEpoch(value: LocalDateTime): Long {
        return value.toInstant(TimeZone.UTC).epochSeconds
    }

    @TypeConverter
    fun fromEpochToLocalDateTime(value: Long): LocalDateTime {
        return Instant.fromEpochSeconds(value).toLocalDateTime(TimeZone.UTC)
    }

    @TypeConverter
    fun fromRepeatTypeToString(value: Recurrence.RepeatType): String {
        return value.value
    }

    @TypeConverter
    fun fromStringToRepeatType(value: String): Recurrence.RepeatType {
        return Recurrence.RepeatType.values().first { it.value == value }
    }

    @TypeConverter
    fun fromListDayOfWeekToString(value: List<DayOfWeek>): String {
        return value.joinToString { it.name }
    }

    @TypeConverter
    fun fromStringToListDayOfWeek(value: String): List<DayOfWeek> {
        return value.split(", ").toTypedArray().map { java.time.DayOfWeek.valueOf(it) }
    }

    @TypeConverter
    fun fromMonthlyTypeToString(value: Recurrence.MonthlyType?) = value?.value

    @TypeConverter
    fun fromStringToMonthlyType(value: String?): Recurrence.MonthlyType? {
        return Recurrence.MonthlyType.values().firstOrNull { it.value == value }
    }

    @TypeConverter
    fun fromEndTypeToString(value: Recurrence.EndType) = value.value

    @TypeConverter
    fun fromStringToEndType(value: String) = Recurrence.EndType.values().first { it.value == value }

    @TypeConverter
    fun fromLocalDateToEpoch(value: LocalDate?) =
        value?.atTime(0, 0)?.toInstant(TimeZone.UTC)?.epochSeconds

    @TypeConverter
    fun fromEpochToLocalDate(value: Long?) =
        value?.let { Instant.fromEpochSeconds(it).toLocalDateTime(TimeZone.UTC).date }
}