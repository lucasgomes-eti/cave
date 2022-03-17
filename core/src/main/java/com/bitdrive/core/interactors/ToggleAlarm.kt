package com.bitdrive.core.interactors

import com.bitdrive.core.data.AlarmRepository
import com.bitdrive.core.domain.Alarm

class ToggleAlarm(private val alarmRepository: AlarmRepository) {
    suspend operator fun invoke(alarm: Alarm) =
        alarmRepository.updateAlarm(alarm.apply { isActive = !isActive })
}