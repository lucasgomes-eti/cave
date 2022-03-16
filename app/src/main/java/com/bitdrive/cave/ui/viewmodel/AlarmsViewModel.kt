package com.bitdrive.cave.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitdrive.core.domain.Alarm
import com.bitdrive.core.interactors.GetAlarms
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmsViewModel @Inject constructor(private val getAlarms: GetAlarms) : ViewModel() {

    val alarms = mutableStateListOf<Alarm>()

    init {
        loadAlarms()
    }

    private fun loadAlarms() {
        viewModelScope.launch {
            getAlarms()
                .distinctUntilChanged()
                .onEach {
                    alarms.clear()
                    alarms.addAll(it)
                }
                .collect()
        }
    }
}