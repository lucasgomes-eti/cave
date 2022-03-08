package com.bitdrive.core.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt
import java.time.format.TextStyle
import java.util.*

data class Recurrence(
    val repeatCount: Int,
    val repeatType: RepeatType,
    val daysOfWeek: List<DayOfWeek>,
    val monthlyOn: MonthlyType?,
    val endType: EndType,
    val endOnDate: LocalDate?,
    val endAfterOccurrencesCount: Int?,
    val createdAt: LocalDate = Clock.System.todayAt(TimeZone.currentSystemDefault())
) {
    enum class RepeatType {
        DAY, WEEK, MONTH, YEAR
    }

    enum class MonthlyType {
        DAY_OF_MONTH, DAY_OF_WEEK_IN_WEEK_OF_MONTH
    }

    enum class EndType {
        NEVER, ON, AFTER
    }

    constructor() : this(
        repeatCount = 1,
        repeatType = RepeatType.DAY,
        daysOfWeek = emptyList(),
        monthlyOn = null,
        endType = EndType.NEVER,
        endOnDate = null,
        endAfterOccurrencesCount = null
    )

    @Suppress("NewApi")
    fun getDisplayName(): String {
        return when (repeatType) {
            RepeatType.DAY -> {
                when(endType) {
                    EndType.NEVER -> {
                        if (repeatCount == 1) "Everyday" else "Every $repeatCount days"
                    }
                    EndType.ON -> "Custom"
                    EndType.AFTER -> "Custom"
                }
            }
            RepeatType.WEEK -> {
                if (repeatCount > 1) "Custom"
                else if (daysOfWeek.containsAll(
                        listOf(
                            DayOfWeek.MONDAY,
                            DayOfWeek.TUESDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY,
                            DayOfWeek.FRIDAY
                        )
                    ) && !daysOfWeek.containsAll(listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
                ) {
                    "Weekdays"
                } else if (daysOfWeek.containsAll(listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)) &&
                    !daysOfWeek.containsAll(
                        listOf(
                            DayOfWeek.MONDAY,
                            DayOfWeek.TUESDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY,
                            DayOfWeek.FRIDAY
                        )
                    )
                ) {
                    "Weekends"
                } else {
                    daysOfWeek.joinToString {
                        it.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        )
                    }
                }
            }
            RepeatType.MONTH -> "Custom"
            RepeatType.YEAR -> "Custom"
        }
    }
}
