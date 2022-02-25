package com.bitdrive.cave.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.bitdrive.cave.framework.AppViewModel
import com.bitdrive.cave.framework.Interactors
import com.bitdrive.core.domain.Alarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmsViewModel(application: Application, interactors: Interactors) :
    AppViewModel(application, interactors) {

    val alarms = mutableStateListOf<Alarm>()

    init {
        viewModelScope.launch {
            loadAlarms()
        }
    }

    private suspend fun loadAlarms() {
        alarms.clear()
        alarms.addAll(interactors.getAlarms())
    }

    fun addAlarm(alarm: Alarm) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                interactors.addAlarm(alarm)
            }
            alarms.add(alarm)
        }
    }
}