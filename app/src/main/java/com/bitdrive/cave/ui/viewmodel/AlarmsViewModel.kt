package com.bitdrive.cave.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitdrive.core.domain.Alarm
import com.bitdrive.core.interactors.GetAlarms
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmsViewModel @Inject constructor(private val getAlarms: GetAlarms) : ViewModel() {

    val alarms = mutableStateListOf<Alarm>()

    init {
        viewModelScope.launch {
            loadAlarms()
        }
    }

    suspend fun loadAlarms() {
        alarms.clear()
        alarms.addAll(getAlarms())
    }
}