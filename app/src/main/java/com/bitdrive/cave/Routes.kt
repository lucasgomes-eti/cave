package com.bitdrive.cave

object Routes {

    data class NewOrEditAlarm(
        private val path: String = "newOrEditAlarm",
        val alarmIdArgument: String = "alarmId"
    ) {
        fun fullPath(alarmId: Long?) = "$path?$alarmIdArgument=${alarmId ?: "{$alarmIdArgument}"}"
    }

    private var _newOrEditAlarm: NewOrEditAlarm? = null
    val newOrEditAlarm: NewOrEditAlarm
        get() {
            if (_newOrEditAlarm != null) {
                return _newOrEditAlarm!!
            }
            _newOrEditAlarm = NewOrEditAlarm()
            return _newOrEditAlarm!!
        }
}