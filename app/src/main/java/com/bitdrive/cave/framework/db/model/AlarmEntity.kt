package com.bitdrive.cave.framework.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bitdrive.core.domain.Alarm
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "alarm")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val datetimeInUtc: LocalDateTime,
    val ringtoneEncodedPath: String?,
    val recurrenceId: Long?,
    val vibrate: Boolean,
    val delete: Boolean,
    var isActive: Boolean,
    val label: String?
) {
    constructor(alarm: Alarm, recurrenceId: Long? = null): this(
        id = alarm.id,
        datetimeInUtc = alarm.datetime,
        ringtoneEncodedPath = alarm.ringtoneEncodedPath,
        recurrenceId = recurrenceId,
        vibrate = alarm.vibrate,
        delete = alarm.delete,
        isActive = alarm.isActive,
        label = alarm.label
    )
}