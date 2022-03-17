package com.bitdrive.core.interactors

import com.bitdrive.core.data.AlarmRepository
import com.bitdrive.core.domain.Alarm

class UpdateAlarm(private val repository: AlarmRepository) {
    suspend operator fun invoke(alarm: Alarm) = repository.updateAlarm(alarm)
}