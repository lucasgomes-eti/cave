package com.bitdrive.cave.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.bitdrive.core.domain.Alarm
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Reminder(alarm: Alarm, modifier: Modifier = Modifier) {
    var checked by remember {
        mutableStateOf(alarm.isActive)
    }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        containerColor = colors.surface,
        shape = RoundedCornerShape(12.dp),
        border = if (checked) BorderStroke(1.dp, colors.onSurface) else null,
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) { // content
            Column { // text
                val dateTimeInCurrentTimeZone =
                    alarm.datetimeInUtc.toInstant(TimeZone.UTC).toLocalDateTime(
                        TimeZone.currentSystemDefault()
                    )
                Text(
                    text = "${
                        String.format(
                            "%02d",
                            dateTimeInCurrentTimeZone.hour
                        )
                    }:${
                        String.format(
                            "%02d",
                            dateTimeInCurrentTimeZone.minute
                        )
                    }" + if (alarm.label.isNullOrBlank()) "" else " | ${alarm.label}",
                    modifier = Modifier.alpha(if (checked) 1f else 0.6f)
                )
                Text(
                    text = if (alarm.repeat == null) "Once" else alarm.repeat!!.getDisplayName(),
                    modifier = Modifier.alpha(if (checked) 1f else 0.6f)
                )
            }
            Column {
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        alarm.isActive = it
                    }
                )
            }
        }


    }
}