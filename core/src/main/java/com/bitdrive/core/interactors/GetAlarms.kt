package com.bitdrive.core.interactors

import com.bitdrive.core.data.AlarmRepository

class GetAlarms(private val alarmRepository: AlarmRepository) {
    operator fun invoke() = alarmRepository.getAlarms()
}