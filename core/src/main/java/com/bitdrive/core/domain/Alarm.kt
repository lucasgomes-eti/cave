package com.bitdrive.core.domain

import kotlin.random.Random

data class Alarm(
    val id: Long,
    val time: Long,
    val ringtone: String,
    val repeat: String,
    val vibrate: Boolean,
    val delete: Boolean,
    var isActive: Boolean,
    val label: String?
) {
    constructor() : this(
        Random.nextLong(),
        1645216278,
        "Default",
        "Once",
        true,
        false,
        true,
        null
    )
}
