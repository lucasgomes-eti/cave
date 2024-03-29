package com.bitdrive.core.interactors

import com.bitdrive.core.data.AlarmRepository

class GetAlarmById(private val alarmRepository: AlarmRepository) {
    suspend operator fun invoke(alarmId: Long) = alarmRepository.getAlarmById(alarmId)
}