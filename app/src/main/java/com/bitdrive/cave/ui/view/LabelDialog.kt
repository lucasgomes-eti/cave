package com.bitdrive.cave.ui.view

import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
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

@Composable
fun LabelDialog(
    modifier: Modifier = Modifier, openDialog: MutableState<Boolean>,
    newOrEditAlarmViewModel: NewOrEditAlarmViewModel
) {
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    AlertDialog(
        modifier = modifier,
        containerColor = colors.surface,
        titleContentColor = colors.onSurface,
        textContentColor = colors.onSurface,
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
                Text(text = "Ok", color = colors.primary)
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