package com.bitdrive.cave.ui.view

import android.app.DatePickerDialog
import android.icu.text.MessageFormat
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.bitdrive.cave.ui.viewmodel.NewOrEditAlarmViewModel
import com.bitdrive.core.domain.Recurrence
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrenceDialog(
    modifier: Modifier = Modifier,
    openDialog: MutableState<Boolean>,
    newOrEditAlarmViewModel: NewOrEditAlarmViewModel
) {
    val scope = rememberCoroutineScope()
    var repeatEveryExpanded by remember {
        mutableStateOf(false)
    }
    var repeatMonthlyExpanded by remember {
        mutableStateOf(false)
    }
    var repeatRecurrence by remember {
        val count = newOrEditAlarmViewModel.recurrence.value?.repeatCount ?: 1
        mutableStateOf(TextFieldValue(count.toString()))
    }
    var afterOccurrences by remember {
        val count = newOrEditAlarmViewModel.recurrence.value?.endAfterOccurrencesCount ?: 1
        mutableStateOf(TextFieldValue(count.toString()))
    }
    val today = Clock.System.todayAt(TimeZone.currentSystemDefault())
    val repeatOptions = listOf("day", "week", "month", "year")
    val daysOfWeekOptions = remember {
        val days = newOrEditAlarmViewModel.recurrence.value?.daysOfWeek ?: emptyList()
        val week = listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        )
        if (days.isEmpty()) {
            mutableStateListOf(
                *week.map { it to (today.dayOfWeek == it) }.toTypedArray()
            )
        } else {
            mutableStateListOf(
                *week.map { it to (days.contains(it)) }.toTypedArray()
            )
        }

    }
    var repeatEverySelectedOption by remember {
        val repeatType =
            newOrEditAlarmViewModel.recurrence.value?.repeatType ?: Recurrence.RepeatType.WEEK
        mutableStateOf(repeatOptions.first { it == repeatType.name.lowercase() })
    }

    val weekOfMonth = today.toJavaLocalDate().get(WeekFields.of(Locale.getDefault()).weekOfMonth())
    val repeatMonthlyOptions = listOf(
        "Monthly on day ${today.dayOfMonth}" to Recurrence.MonthlyType.DAY_OF_MONTH,
        "Monthly on the ${
            MessageFormat.format(
                "{0,ordinal}",
                weekOfMonth
            )
        } ${
            today.dayOfWeek.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            )
        }" to Recurrence.MonthlyType.DAY_OF_WEEK_IN_WEEK_OF_MONTH
    )
    var repeatMonthlySelectedOption by remember {
        mutableStateOf(repeatMonthlyOptions.firstOrNull { it.second == newOrEditAlarmViewModel.recurrence.value?.monthlyOn }
            ?: repeatMonthlyOptions[0])
    }

    val endOnOptions = listOf("Never", "On", "After")
    val (selectedEndOnOption, onEndOnOptionSelected) = remember {
        val endType = newOrEditAlarmViewModel.recurrence.value?.endType ?: Recurrence.EndType.NEVER
        mutableStateOf(endOnOptions.first {
            it == endType.name.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        })
    }

    var selectedEndOnDate by remember {
        mutableStateOf(newOrEditAlarmViewModel.recurrence.value?.endOnDate ?: today)
    }

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            selectedEndOnDate = LocalDate(year, month, dayOfMonth)
        }, selectedEndOnDate.year, selectedEndOnDate.month.value, selectedEndOnDate.dayOfMonth
    )

    AlertDialog(
        modifier = modifier,
        containerColor = colorScheme.surface,
        titleContentColor = colorScheme.onSurface,
        textContentColor = colorScheme.onSurface,
        onDismissRequest = { openDialog.value = false },
        title = {
            Text("Create Recurrence")
        },
        text = {
            Column() { // Container
                Row(verticalAlignment = Alignment.CenterVertically) { // Repeat every
                    Text("Repeat every")
                    Spacer(Modifier.width(8.dp))
                    TextField(
                        value = repeatRecurrence,
                        onValueChange = {
                            repeatRecurrence = it
                        },
                        modifier = Modifier
                            .width(48.dp)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    scope.launch {
                                        delay(10)
                                        val text = repeatRecurrence.text
                                        repeatRecurrence = repeatRecurrence.copy(
                                            selection = TextRange(0, text.length)
                                        )
                                    }
                                } else {
                                    if (repeatRecurrence.text.isEmpty()) {
                                        repeatRecurrence = repeatRecurrence.copy(text = "1")
                                    }
                                }
                            },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = { repeatEveryExpanded = true })
                    )
                    Spacer(Modifier.width(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = repeatEveryExpanded,
                        onExpandedChange = { repeatEveryExpanded = !repeatEveryExpanded },
                        content = {
                            TextField(
                                readOnly = true,
                                value = repeatEverySelectedOption,
                                onValueChange = {},
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = repeatEveryExpanded
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors()
                            )
                            ExposedDropdownMenu(
                                expanded = repeatEveryExpanded,
                                onDismissRequest = { repeatEveryExpanded = false },
                            ) {
                                repeatOptions.forEach {
                                    DropdownMenuItem(
                                        onClick = {
                                            repeatEverySelectedOption = it
                                            repeatEveryExpanded = false
                                        },
                                        text = {
                                            Text(it)
                                        }
                                    )
                                }
                            }
                        })
                }
                Row { // Repeat on
                    if (repeatEverySelectedOption == "week") {
                        Column(Modifier.padding(top = 16.dp)) {
                            Text("Repeat On")
                            LazyRow {
                                items(daysOfWeekOptions.size) { it ->
                                    val item = daysOfWeekOptions[it]
                                    AssistChip(
                                        label = {
                                            Text(
                                                item.first.getDisplayName(
                                                    TextStyle.SHORT,
                                                    Locale.getDefault()
                                                )[0].uppercase(),
                                                color = if (item.second) {
                                                    colorScheme.onPrimary
                                                } else {
                                                    colorScheme.onSurface
                                                }
                                            )
                                        },
                                        shape = CircleShape,
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = if (item.second) {
                                                colorScheme.primary
                                            } else {
                                                colorScheme.surface
                                            },
                                        ),
                                        onClick = {
                                            if (item.first == today.dayOfWeek && daysOfWeekOptions.filter { it.second }.size <= 1)
                                                return@AssistChip

                                            daysOfWeekOptions.removeAt(it)
                                            daysOfWeekOptions.add(
                                                it,
                                                item.copy(second = !item.second)
                                            )

                                            if (daysOfWeekOptions.none { it.second }) {
                                                val itemToday =
                                                    daysOfWeekOptions.first { it.first == today.dayOfWeek }
                                                val indexToday = daysOfWeekOptions.indexOf(
                                                    itemToday
                                                )
                                                daysOfWeekOptions.removeAt(
                                                    indexToday
                                                )
                                                daysOfWeekOptions.add(
                                                    indexToday,
                                                    itemToday.copy(second = true)
                                                )
                                            }

                                        }
                                    )
                                }
                            }
                        }
                    }
                    if (repeatEverySelectedOption == "month") {
                        ExposedDropdownMenuBox(
                            modifier = Modifier.padding(top = 16.dp),
                            expanded = repeatMonthlyExpanded,
                            onExpandedChange = { repeatMonthlyExpanded = !repeatMonthlyExpanded }
                        ) {
                            TextField(
                                readOnly = true,
                                value = repeatMonthlySelectedOption.first,
                                onValueChange = {},
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = repeatEveryExpanded
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors()
                            )
                            ExposedDropdownMenu(
                                expanded = repeatMonthlyExpanded,
                                onDismissRequest = { repeatMonthlyExpanded = false }) {

                                repeatMonthlyOptions.forEach {
                                    DropdownMenuItem(onClick = {
                                        repeatMonthlySelectedOption = it
                                        repeatMonthlyExpanded = false
                                    },
                                        text = {
                                            Text(it.first)
                                        })
                                }
                            }
                        }
                    }
                }
                Row(modifier = Modifier.padding(top = 16.dp)) { // Ends
                    Column {
                        Text("Ends", style = typography.headlineSmall)
                        Column(
                            Modifier
                                .selectableGroup()
                                .padding(top = 16.dp)
                        ) {
                            endOnOptions.forEach { text ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .selectable(
                                            selected = (text == selectedEndOnOption),
                                            onClick = { onEndOnOptionSelected(text) },
                                            role = Role.RadioButton
                                        )
                                        .alpha(if (text == selectedEndOnOption) 1f else .4f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (text == selectedEndOnOption),
                                        onClick = null
                                    )
                                    Text(
                                        text = text,
                                        style = typography.bodyMedium.merge(),
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                    if (text == selectedEndOnOption && selectedEndOnOption == endOnOptions[1]) {
                                        TextField(
                                            modifier = Modifier
                                                .padding(start = 16.dp),
                                            readOnly = true,
                                            singleLine = true,
                                            value = selectedEndOnDate.toJavaLocalDate().format(
                                                DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                                            ),
                                            onValueChange = { },
                                            interactionSource = remember { MutableInteractionSource() } // onClick workaround
                                                .also { interactionSource ->
                                                    LaunchedEffect(interactionSource) {
                                                        interactionSource.interactions.collectIndexed { _, value ->
                                                            if (value is PressInteraction.Release) {
                                                                datePickerDialog.show()
                                                            }
                                                        }
                                                    }
                                                }
                                        )
                                    }
                                    if (text == selectedEndOnOption && selectedEndOnOption == endOnOptions[2]) {
                                        TextField(
                                            modifier = Modifier
                                                .padding(start = 16.dp, end = 8.dp)
                                                .width(48.dp)
                                                .onFocusChanged { focusState ->
                                                    if (focusState.isFocused) {
                                                        scope.launch {
                                                            delay(10)
                                                            val textLength =
                                                                afterOccurrences.text.length
                                                            afterOccurrences =
                                                                afterOccurrences.copy(
                                                                    selection = TextRange(
                                                                        0,
                                                                        textLength
                                                                    )
                                                                )
                                                        }
                                                    } else {
                                                        if (afterOccurrences.text.isEmpty()) {
                                                            afterOccurrences =
                                                                afterOccurrences.copy(text = "1")
                                                        }
                                                    }
                                                },
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            value = afterOccurrences,
                                            onValueChange = { afterOccurrences = it }
                                        )
                                        Text("Occurrences")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    newOrEditAlarmViewModel.createRecurrence(
                        repeatRecurrence = repeatRecurrence.text,
                        repeatEverySelectedOption = repeatEverySelectedOption,
                        daysOfWeekOptions = daysOfWeekOptions,
                        repeatMonthlySelectedOption = repeatMonthlySelectedOption.second,
                        selectedEndOnOption = selectedEndOnOption,
                        selectedEndOnDate = selectedEndOnDate,
                        afterOccurrences = afterOccurrences.text
                    )
                    openDialog.value = false
                },
            ) {
                Text("Done", color = colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { openDialog.value = false },
            ) {
                Text("Cancel", color = colorScheme.secondary)
            }
        }
    )
}