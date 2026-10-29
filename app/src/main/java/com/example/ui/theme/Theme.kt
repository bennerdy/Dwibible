package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DwibibleDarkColorScheme = darkColorScheme(
    primary = DarkDwibiblePalette.celestialBlue,
    onPrimary = DarkDwibiblePalette.background,
    primaryContainer = DarkDwibiblePalette.popupSurface,
    onPrimaryContainer = DarkDwibiblePalette.textPrimary,
    secondary = DarkDwibiblePalette.celestialBlueLight,
    onSecondary = DarkDwibiblePalette.background,
    tertiary = DarkDwibiblePalette.crimsonTriangle,
    background = DarkDwibiblePalette.background,
    onBackground = DarkDwibiblePalette.textPrimary,
    surface = DarkDwibiblePalette.cardSurface,
    onSurface = DarkDwibiblePalette.textPrimary,
    surfaceVariant = DarkDwibiblePalette.popupSurface,
    onSurfaceVariant = DarkDwibiblePalette.textSecondary,
    outline = DarkDwibiblePalette.cardBorder
)

private val DwibibleLightColorScheme = lightColorScheme(
    primary = LightDwibiblePalette.celestialBlue,
    onPrimary = LightDwibiblePalette.cardSurface,
    primaryContainer = LightDwibiblePalette.popupSurface,
    onPrimaryContainer = LightDwibiblePalette.textPrimary,
    secondary = LightDwibiblePalette.celestialBlueLight,
    onSecondary = LightDwibiblePalette.cardSurface,
    tertiary = LightDwibiblePalette.crimsonTriangle,
    background = LightDwibiblePalette.background,
    onBackground = LightDwibiblePalette.textPrimary,
    surface = LightDwibiblePalette.cardSurface,
    onSurface = LightDwibiblePalette.textPrimary,
    surfaceVariant = LightDwibiblePalette.popupSurface,
    onSurfaceVariant = LightDwibiblePalette.textSecondary,
    outline = LightDwibiblePalette.cardBorder
)

@Composable
fun DwibibleTheme(
    isDarkMode: Boolean = true,
    content: @Composable () -> Unit
) {
    val palette = if (isDarkMode) DarkDwibiblePalette else LightDwibiblePalette
    val colorScheme = if (isDarkMode) DwibibleDarkColorScheme else DwibibleLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !isDarkMode
                controller.isAppearanceLightNavigationBars = !isDarkMode
            }
        }
    }

    CompositionLocalProvider(LocalDwibibleColors provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
