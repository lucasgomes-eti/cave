package com.bitdrive.cave.framework.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bitdrive.core.domain.Recurrence
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Entity(tableName = "recurrence")
data class RecurrenceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val repeatCount: Int,
    val repeatType: Recurrence.RepeatType,
    val daysOfWeek: List<DayOfWeek>,
    val monthlyOn: Recurrence.MonthlyType?,
    val endType: Recurrence.EndType,
    val endOnDate: LocalDate?,
    val endAfterOccurrencesCount: Int?,
    val createdAt: LocalDate
) {
    constructor(recurrence: Recurrence) : this(
        id = recurrence.id,
        repeatCount = recurrence.repeatCount,
        repeatType = recurrence.repeatType,
        daysOfWeek = recurrence.daysOfWeek,
        monthlyOn = recurrence.monthlyOn,
        endType = recurrence.endType,
        endOnDate = recurrence.endOnDate,
        endAfterOccurrencesCount = recurrence.endAfterOccurrencesCount,
        createdAt = recurrence.createdAt
    )

    fun map() = Recurrence(
        id = id,
        repeatCount = repeatCount,
        repeatType = repeatType,
        daysOfWeek = daysOfWeek,
        monthlyOn = monthlyOn,
        endType = endType,
        endOnDate = endOnDate,
        endAfterOccurrencesCount = endAfterOccurrencesCount,
        createdAt = createdAt
    )
}
