package com.bitdrive.cave.framework

import com.bitdrive.core.interactors.AddAlarm
import com.bitdrive.core.interactors.GetAlarms

data class Interactors(
    val addAlarm: AddAlarm,
    val getAlarms: GetAlarms
)