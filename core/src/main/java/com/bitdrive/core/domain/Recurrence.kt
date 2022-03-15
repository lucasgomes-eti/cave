package com.bitdrive.core.domain

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.time.format.TextStyle
import java.util.*
import kotlin.random.Random

data class Recurrence(
    val id: Long = Random.nextLong(),
    val repeatCount: Int,
    val repeatType: RepeatType,
    val daysOfWeek: List<DayOfWeek>,
    val monthlyOn: MonthlyType?,
    val endType: EndType,
    val endOnDate: LocalDate?,
    val endAfterOccurrencesCount: Int?,
    val createdAt: LocalDate = Clock.System.todayAt(TimeZone.currentSystemDefault())
) {
    enum class RepeatType(val value: String) {
        DAY("day"), WEEK("week"), MONTH("month"), YEAR("year")
    }

    enum class MonthlyType(val value: String) {
        DAY_OF_MONTH("day_of_month"), DAY_OF_WEEK_IN_WEEK_OF_MONTH("day_of_week_in_week_of_month")
    }

    enum class EndType(val value: String) {
        NEVER("never"), ON("on"), AFTER("after")
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
                when (endType) {
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
