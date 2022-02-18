package com.bitdrive.cave.ui.components

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Reminder() {
    var checked by remember {
        mutableStateOf(true)
    }
    Card(
        modifier = Modifier
            .padding(16.dp)
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
                Text(text = "06:00 Wake Up", modifier = Modifier.alpha(if (checked) 1f else 0.6f))
                Text(text = "Monday to Friday", modifier = Modifier.alpha(if (checked) 1f else 0.6f))
            }
            Column {
                Switch(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }
        }


    }
}