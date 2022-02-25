package com.bitdrive.cave.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    val modalBottomState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    ModalBottomSheetLayout(
        sheetState = modalBottomState,
        sheetContent = {
            Column {
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
                            .clickable { scope.launch { modalBottomState.hide() } },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Close,
                            null,
                            Modifier
                                .size(24.dp),
                            colors.onSurface
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Add alarm")
                        Text("Alarm in 23 hours 59 minutes")
                    }
                    Column(
                        Modifier
                            .size(48.dp)
                            .clickable { scope.launch { modalBottomState.hide() } },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Check,
                            null,
                            Modifier
                                .size(24.dp),
                            colors.onSurface
                        )
                    }
                }
                ListItem(
                    text = { Text("14:23") },
                    overlineText = { Text("Time") },
                    trailing = {
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            null,
                            tint = colors.onSurface
                        )
                    },
                    modifier = Modifier.clickable { }
                )
                ListItem(
                    text = { Text("Ringtone") },
                    secondaryText = { Text("Default Ringtone") },
                    trailing = {
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            null,
                            tint = colors.onSurface
                        )
                    },
                    modifier = Modifier.clickable { }
                )
                ListItem(
                    text = { Text("Repeat") },
                    secondaryText = { Text("Once") },
                    trailing = {
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            null,
                            tint = colors.onSurface
                        )
                    },
                    modifier = Modifier.clickable { }
                )
                var vibrate by remember { mutableStateOf(true) }
                ListItem(
                    text = { Text("Vibrate when alarm sounds") },
                    trailing = {
                        Switch(
                            checked = vibrate,
                            onCheckedChange = { vibrate = it }
                        )
                    },
                    modifier = Modifier.clickable { vibrate = !vibrate }
                )
                var delete by remember { mutableStateOf(false) }
                ListItem(
                    text = { Text("Delete after goes off") },
                    trailing = {
                        Switch(
                            checked = delete,
                            onCheckedChange = { delete = it }
                        )
                    },
                    modifier = Modifier.clickable { delete = !delete }
                )
                ListItem(
                    text = { Text("Label") },
                    trailing = {
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            null,
                            tint = colors.onSurface
                        )
                    },
                    modifier = Modifier.clickable { }
                )
            }
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
                        scope.launch { modalBottomState.show() }
                    },
                    shape = RoundedCornerShape(16.dp),
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReminders() {
    CaveTheme(darkTheme = true) {
        RemindersView()
    }
}