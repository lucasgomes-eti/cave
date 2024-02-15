package com.bitdrive.cave.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bitdrive.core.domain.Alarm
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@Composable
fun Reminder(
    alarm: Alarm,
    modifier: Modifier = Modifier,
    onCheckedChange: () -> Unit
) {
    var isAlarmActive by remember {
        mutableStateOf(alarm.isActive)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth().clip(RoundedCornerShape(12.dp)).then(modifier),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = if (isAlarmActive) BorderStroke(1.dp, colorScheme.onSurface) else null,
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) { // content
            Column { // text
                Text(
                    text = "${
                        String.format(
                            "%02d",
                            alarm.datetime.hour
                        )
                    }:${
                        String.format(
                            "%02d",
                            alarm.datetime.minute
                        )
                    }" + if (alarm.label.isNullOrBlank()) "" else " | ${alarm.label}",
                    modifier = Modifier.alpha(if (isAlarmActive) 1f else 0.6f)
                )
                Text(
                    text = if (alarm.repeat == null) "Once" else alarm.repeat!!.getDisplayName(),
                    modifier = Modifier.alpha(if (isAlarmActive) 1f else 0.6f)
                )
            }
            Column {
                Switch(
                    checked = isAlarmActive,
                    onCheckedChange = { checked ->
                        onCheckedChange()
                        isAlarmActive = checked
                    }
                )
            }
        }
    }
}