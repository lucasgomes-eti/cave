package com.bitdrive.cave.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bitdrive.cave.ui.theme.CaveTheme

@Composable
fun LifeHacksView() {
    Column {
        Text(text = "Life Hacks")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLifeHacks() {
    CaveTheme {
        ShopView()
    }
}