package com.bitdrive.cave.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bitdrive.cave.Routes
import com.bitdrive.cave.ui.components.Reminder
import com.bitdrive.cave.ui.viewmodel.AlarmsViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun RemindersView(viewModel: AlarmsViewModel, navController: NavController) {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    Scaffold(
        containerColor = colorScheme.background,
        contentColor = colorScheme.onBackground,
        content = { _ ->
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.alarms) { alarm ->
                    Reminder(alarm, Modifier.clickable {
                        scope.launch {
                            navController.navigate(Routes.newOrEditAlarm.fullPath(alarm.id))
                        }
                    }) {
                        viewModel.toggle(alarm)
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
                content = { Icon(Icons.Filled.Add, null, tint = colorScheme.onPrimary) },
                onClick = {
                    scope.launch { navController.navigate(Routes.newOrEditAlarm.fullPath(null)) }
                },
                shape = RoundedCornerShape(16.dp),
            )
        }
    )
}