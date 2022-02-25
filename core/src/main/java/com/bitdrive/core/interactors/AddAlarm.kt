package com.bitdrive.core.interactors

import com.bitdrive.core.data.AlarmRepository
import com.bitdrive.core.domain.Alarm

class AddAlarm(private val alarmRepository: AlarmRepository) {
    suspend operator fun invoke(alarm: Alarm) = alarmRepository.addAlarm(alarm)
}