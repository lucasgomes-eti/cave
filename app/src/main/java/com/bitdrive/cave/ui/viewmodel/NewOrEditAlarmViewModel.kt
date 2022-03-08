package com.bitdrive.cave.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import com.bitdrive.cave.framework.AppViewModel
import com.bitdrive.cave.framework.Interactors
import com.bitdrive.core.domain.Alarm
import com.bitdrive.core.domain.Recurrence
import kotlinx.datetime.*

class NewOrEditAlarmViewModel(application: Application, interactors: Interactors) :
    AppViewModel(application, interactors) {

    var selectedDateTime =
        mutableStateOf(Clock.System.now().plus(DateTimePeriod(hours = 1), TimeZone.UTC))

    var ringtoneResult = mutableStateOf<Uri?>(null)

    var recurrence = mutableStateOf<Recurrence?>(null)

    val vibrate = mutableStateOf(true)

    val delete = mutableStateOf(false)

    val label = mutableStateOf(TextFieldValue(""))

    fun createRecurrence(
        repeatRecurrence: String,
        repeatEverySelectedOption: String,
        daysOfWeekOptions: SnapshotStateList<Pair<DayOfWeek, Boolean>>,
        repeatMonthlySelectedOption: Recurrence.MonthlyType,
        selectedEndOnOption: String,
        selectedEndOnDate: LocalDate,
        afterOccurrences: String
    ) {
        val repeatType = when (repeatEverySelectedOption) {
            "day" -> Recurrence.RepeatType.DAY
            "week" -> Recurrence.RepeatType.WEEK
            "month" -> Recurrence.RepeatType.MONTH
            "year" -> Recurrence.RepeatType.YEAR
            else -> Recurrence.RepeatType.DAY
        }
        val endType = when (selectedEndOnOption) {
            "Never" -> Recurrence.EndType.NEVER
            "On" -> Recurrence.EndType.ON
            "After" -> Recurrence.EndType.AFTER
            else -> Recurrence.EndType.NEVER
        }
        recurrence.value = Recurrence(
            repeatCount = try {
                repeatRecurrence.toInt()
            } catch (e: NumberFormatException) {
                1
            },
            repeatType = repeatType,
            daysOfWeek = if (repeatType == Recurrence.RepeatType.WEEK) daysOfWeekOptions.filter { it.second }
                .map { it.first } else emptyList(),
            monthlyOn = if (repeatType == Recurrence.RepeatType.MONTH) repeatMonthlySelectedOption else null,
            endType = endType,
            endOnDate = if (endType == Recurrence.EndType.ON) selectedEndOnDate else null,
            endAfterOccurrencesCount = if (endType == Recurrence.EndType.AFTER) try {
                afterOccurrences.toInt()
            } catch (e: java.lang.NumberFormatException) {
                1
            } else null
        )
    }

    suspend fun addAlarm() {
        interactors.addAlarm(
            Alarm(
                datetimeInUtc = selectedDateTime.value.toLocalDateTime(TimeZone.UTC),
                ringtoneEncodedPath = ringtoneResult.value?.encodedPath,
                repeat = recurrence.value,
                vibrate = vibrate.value,
                delete = delete.value,
                isActive = true,
                label = label.value.text.ifEmpty { null }
            )
        )

    }
}