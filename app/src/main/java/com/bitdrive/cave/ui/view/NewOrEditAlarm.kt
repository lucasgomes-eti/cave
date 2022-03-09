package com.bitdrive.cave.ui.view

import android.app.Activity.RESULT_OK
import android.app.TimePickerDialog
import android.content.Intent
import android.media.RingtoneManager.*
import android.text.format.DateFormat.is24HourFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bitdrive.cave.ui.viewmodel.NewOrEditAlarmViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewOrEditAlarm(
    modifier: Modifier = Modifier,
    modalState: ModalBottomSheetState,
    viewModel: NewOrEditAlarmViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var ringtoneResult by remember {
        viewModel.ringtoneResult
    }
    val ringtoneLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (it.resultCode == RESULT_OK && it.data != null) {
                ringtoneResult = it.data!!.getParcelableExtra(EXTRA_RINGTONE_PICKED_URI)
            }
        })
    var selectedDateTime by remember {
        viewModel.selectedDateTime
    }
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val dateTime = selectedDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
            val timeInCurrentTimeZone = LocalDateTime(
                year = dateTime.year,
                monthNumber = dateTime.monthNumber,
                dayOfMonth = dateTime.dayOfMonth,
                hour = hourOfDay,
                minute = minute
            )
            val dateTimeInCurrentTimeZone =
                timeInCurrentTimeZone.toInstant(TimeZone.currentSystemDefault())
            selectedDateTime = dateTimeInCurrentTimeZone.toLocalDateTime(TimeZone.UTC).toInstant(
                TimeZone.UTC
            )
        },
        selectedDateTime.toLocalDateTime(TimeZone.currentSystemDefault()).hour,
        selectedDateTime.toLocalDateTime(TimeZone.currentSystemDefault()).minute,
        is24HourFormat(LocalContext.current)
    )
    val openRecurrenceDialog = remember { mutableStateOf(false) }
    if (openRecurrenceDialog.value) {
        RecurrenceDialog(openDialog = openRecurrenceDialog, newOrEditAlarmViewModel = viewModel)
    }
    val openLabelDialog = remember { mutableStateOf(false) }
    if (openLabelDialog.value) {
        LabelDialog(openDialog = openLabelDialog, newOrEditAlarmViewModel = viewModel)
    }
    Column(modifier = modifier) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .size(48.dp)
                    .clickable { scope.launch { modalState.hide() } },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Close,
                    null,
                    Modifier
                        .size(24.dp),
                    MaterialTheme.colors.onSurface
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val timeUntilAlarm = Clock.System.now().periodUntil(selectedDateTime, TimeZone.UTC)
                Text("Add alarm")
                Text(
                    text = if (timeUntilAlarm.hours >= 1) {
                        "Alarm in ${timeUntilAlarm.hours} hours and ${timeUntilAlarm.minutes} minutes"
                    } else {
                        "Alarm in ${timeUntilAlarm.minutes} minutes"
                    }
                )
            }
            Column(
                Modifier
                    .size(48.dp)
                    .clickable {
                        scope.launch {
                            modalState.hide()
                            scope.launch { viewModel.addAlarm() }
                        }
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Check,
                    null,
                    Modifier
                        .size(24.dp),
                    MaterialTheme.colors.onSurface
                )
            }
        }
        ListItem(
            text = {
                val datetimeInSystemZone =
                    selectedDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
                Text(
                    "${String.format("%02d", datetimeInSystemZone.hour)}:${
                        String.format(
                            "%02d",
                            datetimeInSystemZone.minute
                        )
                    }"
                )
            },
            overlineText = { Text("Time") },
            trailing = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    null,
                    tint = MaterialTheme.colors.onSurface
                )
            },
            modifier = Modifier.clickable { timePickerDialog.show() }
        )
        ListItem(
            text = { Text("Ringtone") },
            secondaryText = if (ringtoneResult == null) {
                null
            } else {
                { Text("${ringtoneResult!!.lastPathSegment}") }
            },
            trailing = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    null,
                    tint = MaterialTheme.colors.onSurface
                )
            },
            modifier = Modifier.clickable {
                val intent = Intent(ACTION_RINGTONE_PICKER).apply {
                    putExtra(EXTRA_RINGTONE_TYPE, TYPE_ALARM)
                    putExtra(EXTRA_RINGTONE_TITLE, "Select alarm tone")
                    putExtra(EXTRA_RINGTONE_SHOW_SILENT, false)
                    putExtra(EXTRA_RINGTONE_SHOW_DEFAULT, true)
                }
                ringtoneLauncher.launch(intent)
            }
        )
        ListItem(
            text = { Text("Repeat") },
            secondaryText = {
                Text(
                    if (viewModel.recurrence.value == null) "Once" else {
                        viewModel.recurrence.value!!.getDisplayName()
                    }
                )
            },
            trailing = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    null,
                    tint = MaterialTheme.colors.onSurface
                )
            },
            modifier = Modifier.clickable {
                openRecurrenceDialog.value = true
            }
        )
        ListItem(
            text = { Text("Vibrate when alarm sounds") },
            trailing = {
                Switch(
                    checked = viewModel.vibrate.value,
                    onCheckedChange = { viewModel.vibrate.value = it }
                )
            },
            modifier = Modifier.clickable { viewModel.vibrate.value = !viewModel.vibrate.value }
        )
        ListItem(
            text = { Text("Delete after goes off") },
            trailing = {
                Switch(
                    checked = viewModel.delete.value,
                    onCheckedChange = { viewModel.delete.value = it }
                )
            },
            modifier = Modifier.clickable { viewModel.delete.value = !viewModel.delete.value }
        )
        ListItem(
            text = { Text("Label") },
            trailing = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    null,
                    tint = MaterialTheme.colors.onSurface
                )
            },
            modifier = Modifier.clickable {
                openLabelDialog.value = true
            },
            secondaryText = if (viewModel.label.value.text.isEmpty()) null else {
                { Text(text = viewModel.label.value.text) }
            }
        )
    }
}