package com.bitdrive.core.interactors

import com.bitdrive.core.data.AlarmRepository

class GetAlarms(private val alarmRepository: AlarmRepository) {
    suspend operator fun invoke() = alarmRepository.getAlarms()
}