package com.bitdrive.cave.ui.view

import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextRange
import com.bitdrive.cave.ui.viewmodel.NewOrEditAlarmViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelDialog(
    modifier: Modifier = Modifier, openDialog: MutableState<Boolean>,
    newOrEditAlarmViewModel: NewOrEditAlarmViewModel
) {
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    AlertDialog(
        modifier = modifier,
        containerColor = colorScheme.surface,
        titleContentColor = colorScheme.onSurface,
        textContentColor = colorScheme.onSurface,
        onDismissRequest = { openDialog.value = false },
        title = { Text("Add alarm label") },
        text = {
            TextField(
                value = newOrEditAlarmViewModel.label.value,
                onValueChange = { newOrEditAlarmViewModel.label.value = it },
                placeholder = { Text("Enter label") },
                modifier = Modifier.focusRequester(focusRequester)
            )
        },
        confirmButton = {
            TextButton(onClick = { openDialog.value = false }) {
                Text(text = "Ok", color = colorScheme.primary)
            }
        }
    )

    LaunchedEffect(Unit) {
        delay(300)
        inputService?.showSoftwareKeyboard()
        focusRequester.requestFocus()
        newOrEditAlarmViewModel.label.value = newOrEditAlarmViewModel.label.value.copy(
            selection = TextRange(
                newOrEditAlarmViewModel.label.value.text.length
            )
        )
    }
}