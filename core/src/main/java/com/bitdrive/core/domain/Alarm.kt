package com.bitdrive.core.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

data class Alarm(
    val id: Long = Random.nextLong(),
    val datetimeInUtc: LocalDateTime,
    val ringtoneEncodedPath: String?,
    val repeat: Recurrence?,
    val vibrate: Boolean,
    val delete: Boolean,
    var isActive: Boolean,
    val label: String?
) {
    constructor() : this(
        datetimeInUtc = Clock.System.now().toLocalDateTime(TimeZone.UTC),
        ringtoneEncodedPath = null,
        repeat = null,
        vibrate = true,
        delete = false,
        isActive = true,
        label = null
    )
}
