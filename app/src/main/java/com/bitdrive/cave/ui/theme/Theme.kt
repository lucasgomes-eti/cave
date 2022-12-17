package com.bitdrive.cave.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = LavenderBlue,
    primaryContainer = DarkSlateBlue,
    secondary = LightPeriwinkle,
    secondaryContainer = CameoPink,
    background = EerieBlack,
    surface = CharlestonGreen,
    error = Melon,
    onPrimary = PersianIndigo,
    onSecondary = Onyx,
    onBackground = Platinum,
    onSurface = Platinum,
    onError = Blood
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColorScheme(
    primary = RoyalPurple,
    primaryContainer = Lavender,
    secondary = BlackCoral,
    secondaryContainer = DeepTaupe,
    background = Color.White,
    surface = Color.White,
    error = Firebrick,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = EerieBlack,
    onSurface = EerieBlack,
    onError = Color.White
)

@Composable
fun CaveTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}