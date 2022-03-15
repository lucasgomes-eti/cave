package com.bitdrive.cave.framework.db.vo

import androidx.room.Embedded
import androidx.room.Relation
import com.bitdrive.cave.framework.db.model.AlarmEntity
import com.bitdrive.cave.framework.db.model.RecurrenceEntity
import com.bitdrive.core.domain.Alarm

data class AlarmVO(
    @Embedded
    val alarmEntity: AlarmEntity,
    @Relation(
        parentColumn = "recurrenceId",
        entityColumn = "id"
    )
    val recurrenceEntity: RecurrenceEntity?
) {
    fun map() = Alarm(
        id = alarmEntity.id!!,
        datetimeInUtc = alarmEntity.datetimeInUtc,
        ringtoneEncodedPath = alarmEntity.ringtoneEncodedPath,
        repeat = recurrenceEntity?.map(),
        vibrate = alarmEntity.vibrate,
        delete = alarmEntity.delete,
        isActive = alarmEntity.isActive,
        label = alarmEntity.label
    )
}