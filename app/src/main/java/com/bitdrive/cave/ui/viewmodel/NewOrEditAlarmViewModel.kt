package com.bitdrive.cave.ui.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitdrive.cave.AlarmReceiver
import com.bitdrive.cave.Routes
import com.bitdrive.core.domain.Alarm
import com.bitdrive.core.domain.Recurrence
import com.bitdrive.core.interactors.AddAlarm
import com.bitdrive.core.interactors.DeleteAlarm
import com.bitdrive.core.interactors.GetAlarmById
import com.bitdrive.core.interactors.UpdateAlarm
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class NewOrEditAlarmViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getAlarmById: GetAlarmById,
    private val addAlarm: AddAlarm,
    private val updateAlarm: UpdateAlarm,
    private val deleteAlarm: DeleteAlarm,
    private val alarmManager: AlarmManager,
    private val appContext: Context
) : ViewModel() {

    var selectedDateTime =
        mutableStateOf(Clock.System.now().plus(DateTimePeriod(hours = 1), TimeZone.currentSystemDefault()))

    var ringtoneResult = mutableStateOf<Uri?>(null)

    var recurrence = mutableStateOf<Recurrence?>(null)

    val vibrate = mutableStateOf(true)

    val delete = mutableStateOf(false)

    val label = mutableStateOf(TextFieldValue(""))

    private val alarmId: Long?
        get() {
            val value: String? = savedStateHandle[Routes.newOrEditAlarm.alarmIdArgument]
            return value?.toLongOrNull()
        }
    val isEditing = alarmId != null

    init {
        if (isEditing) {
            viewModelScope.launch { bindAlarm(getAlarmById(alarmId!!)) }
        }
    }

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

    suspend fun saveAlarm() {
        val alarm = Alarm(
            datetime = selectedDateTime.value.toLocalDateTime(TimeZone.currentSystemDefault()),
            ringtoneEncodedPath = ringtoneResult.value?.encodedPath,
            repeat = recurrence.value,
            vibrate = vibrate.value,
            delete = delete.value,
            isActive = true,
            label = label.value.text.ifEmpty { null }
        )
        if (alarmId == null) {
            addAlarm(alarm)
        } else {
            updateAlarm(alarm.copy(id = alarmId))
        }

        val intent = Intent(appContext, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            appContext, alarm.hashCode(), intent,
            PendingIntent.FLAG_IMMUTABLE
        )

//        alarmManager.setAlarmClock(
//            AlarmManager.AlarmClockInfo(
//                alarm.datetime.toInstant(
//                    TimeZone.currentSystemDefault()
//                ).toEpochMilliseconds(), pendingIntent
//            ), pendingIntent
//        )
    }

    fun bindAlarm(alarm: Alarm) {
        selectedDateTime.value = alarm.datetime.toInstant(TimeZone.currentSystemDefault())
        ringtoneResult.value =
            alarm.ringtoneEncodedPath?.let { Uri.Builder().encodedPath(it).build() }
        recurrence.value = alarm.repeat
        vibrate.value = alarm.vibrate
        delete.value = alarm.delete
        label.value = TextFieldValue(alarm.label ?: "")
    }

    fun deleteAlarm() {
        viewModelScope.launch {
            val alarm = Alarm(
                id = alarmId,
                datetime = selectedDateTime.value.toLocalDateTime(TimeZone.currentSystemDefault()),
                ringtoneEncodedPath = ringtoneResult.value?.encodedPath,
                repeat = recurrence.value,
                vibrate = vibrate.value,
                delete = delete.value,
                isActive = true,
                label = label.value.text.ifEmpty { null }
            )
            deleteAlarm(alarm)
        }
    }
}