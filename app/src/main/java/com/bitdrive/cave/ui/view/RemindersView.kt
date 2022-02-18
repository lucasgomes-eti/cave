package com.bitdrive.cave.ui.view

import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.bitdrive.cave.ui.components.Reminder
import com.bitdrive.cave.ui.theme.CaveTheme

@Composable
fun RemindersView(navController: NavController? = null) {
        Surface(
            color = colors.background,
            contentColor = colors.onBackground,
        ) {
            Reminder()
        }
}

@Preview(showBackground = true)
@Composable
fun PreviewReminders() {
    CaveTheme(darkTheme = true) {
        RemindersView()
    }
}