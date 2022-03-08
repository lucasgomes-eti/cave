package com.bitdrive.cave.ui.view

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bitdrive.cave.framework.AppViewModelFactory
import com.bitdrive.cave.ui.components.Reminder
import com.bitdrive.cave.ui.theme.CaveTheme
import com.bitdrive.cave.ui.viewmodel.AlarmsViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun RemindersView() {
    val viewModel = viewModel<AlarmsViewModel>(
        viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
        factory = AppViewModelFactory,
        key = "Alarms"
    )
    val modalState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    if(!modalState.isVisible) {
        scope.launch { viewModel.loadAlarms() }
    }

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetContent = {
            NewOrEditAlarm(modalState = modalState)
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
                    items(
                        items = viewModel.alarms,
                        key = { it.id }) {
                        Reminder(
                            it,
                            Modifier.animateItemPlacement()
                        )
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
        RemindersView()
    }
}