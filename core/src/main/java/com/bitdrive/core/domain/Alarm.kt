package com.bitdrive.core.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Alarm(
    val id: Long? = null,
    val datetime: LocalDateTime,
    val ringtoneEncodedPath: String?,
    val repeat: Recurrence?,
    val vibrate: Boolean,
    val delete: Boolean,
    var isActive: Boolean,
    val label: String?
) {
    constructor() : this(
        datetime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        ringtoneEncodedPath = null,
        repeat = null,
        vibrate = true,
        delete = false,
        isActive = true,
        label = null
    )
}
