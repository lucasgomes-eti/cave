package com.bitdrive.cave.ui.view

import android.app.Activity.RESULT_OK
import android.app.TimePickerDialog
import android.content.Intent
import android.media.RingtoneManager.*
import android.text.format.DateFormat.is24HourFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.bitdrive.cave.ui.viewmodel.NewOrEditAlarmViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewOrEditAlarm(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
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

    val openDeleteDialog = remember { mutableStateOf(false) }
    if (openDeleteDialog.value) {
        AlertDialog(
            containerColor = colorScheme.surface,
            textContentColor = colorScheme.onSurface,
            text = { Text("Delete alarm?") },
            confirmButton = {
                TextButton(onClick = {
                    openDeleteDialog.value = false
                    scope.launch { drawerState.close() }
                    viewModel.deleteAlarm()
                }) {
                    Text("Delete", color = colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { openDeleteDialog.value = false }) {
                    Text("Cancel", color = colorScheme.secondary)
                }
            },
            onDismissRequest = { openDeleteDialog.value = false }
        )
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
                    .clickable { scope.launch { drawerState.close() } },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Close,
                    null,
                    Modifier
                        .size(24.dp),
                    colorScheme.onSurface
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
                            drawerState.close()
                            scope.launch { viewModel.saveAlarm() }
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
                    colorScheme.onSurface
                )
            }
        }
        ListItem(
            headlineText = {
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
            trailingContent = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    null,
                    tint = colorScheme.onSurface
                )
            },
            modifier = Modifier.clickable { timePickerDialog.show() }
        )
        ListItem(
            headlineText = { Text("Ringtone") },
            supportingText = if (ringtoneResult == null) {
                null
            } else {
                { Text("${ringtoneResult!!.lastPathSegment}") }
            },
            trailingContent = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    null,
                    tint = colorScheme.onSurface
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
            headlineText = { Text("Repeat") },
            supportingText = {
                Text(
                    if (viewModel.recurrence.value == null) "Once" else {
                        viewModel.recurrence.value!!.getDisplayName()
                    }
                )
            },
            trailingContent = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    null,
                    tint = colorScheme.onSurface
                )
            },
            modifier = Modifier.clickable {
                openRecurrenceDialog.value = true
            }
        )
        ListItem(
            headlineText = { Text("Vibrate when alarm sounds") },
            trailingContent = {
                Switch(
                    checked = viewModel.vibrate.value,
                    onCheckedChange = { viewModel.vibrate.value = it }
                )
            },
            modifier = Modifier.clickable { viewModel.vibrate.value = !viewModel.vibrate.value }
        )
        ListItem(
            headlineText = { Text("Delete after goes off") },
            trailingContent = {
                Switch(
                    checked = viewModel.delete.value,
                    onCheckedChange = { viewModel.delete.value = it }
                )
            },
            modifier = Modifier.clickable { viewModel.delete.value = !viewModel.delete.value }
        )
        ListItem(
            headlineText = { Text("Label") },
            trailingContent = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    null,
                    tint = colorScheme.onSurface
                )
            },
            modifier = Modifier.clickable {
                openLabelDialog.value = true
            },
            supportingText = if (viewModel.label.value.text.isEmpty()) null else {
                { Text(text = viewModel.label.value.text) }
            }
        )
        if (viewModel.alarmId.value != null) {
            ListItem(
                headlineText = { Text("Delete", style = TextStyle(color = colorScheme.onError)) },
                trailingContent = {
                    Icon(
                        Icons.Default.Delete,
                        null,
                        tint = colorScheme.onError
                    )
                },
                modifier = Modifier
                    .background(color = colorScheme.error)
                    .clickable {
                        openDeleteDialog.value = true
                    }
            )
        }
    }
}