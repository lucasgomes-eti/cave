package com.bitdrive.cave.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = LavenderBlue,
    primaryVariant = DarkSlateBlue,
    secondary = LightPeriwinkle,
    secondaryVariant = CameoPink,
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
private val LightColorPalette = lightColors(
    primary = RoyalPurple,
    primaryVariant = Lavender,
    secondary = BlackCoral,
    secondaryVariant = DeepTaupe,
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
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}