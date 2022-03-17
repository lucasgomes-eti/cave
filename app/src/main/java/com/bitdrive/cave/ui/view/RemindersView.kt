package com.bitdrive.cave.ui.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bitdrive.cave.ui.components.Reminder
import com.bitdrive.cave.ui.theme.CaveTheme
import com.bitdrive.cave.ui.viewmodel.AlarmsViewModel
import com.bitdrive.cave.ui.viewmodel.NewOrEditAlarmViewModel
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun RemindersView(viewModel: AlarmsViewModel) {
    val modalState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    val newOrEditAlarmViewModel = hiltViewModel<NewOrEditAlarmViewModel>()

    if (!modalState.isVisible) {
        newOrEditAlarmViewModel.reset()
    }

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetContent = {
            NewOrEditAlarm(modalState = modalState, viewModel = newOrEditAlarmViewModel)
        }) {
        Scaffold(
            containerColor = colors.background,
            contentColor = colors.onBackground,
            content = {
                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(viewModel.alarms) {
                        Reminder(it, Modifier.clickable {
                            newOrEditAlarmViewModel.bindAlarm(it)
                            scope.launch { modalState.show() }
                        }) { viewModel.toggle(it) }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = colors.primary,
                    contentColor = colors.onPrimary,
                    content = { Icon(Icons.Filled.Add, null, tint = colors.onPrimary) },
                    onClick = {
                        scope.launch { modalState.show() }
                    },
                    shape = RoundedCornerShape(16.dp),
                )
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewReminders() {
    CaveTheme(darkTheme = true) {
        //RemindersView()
    }
}