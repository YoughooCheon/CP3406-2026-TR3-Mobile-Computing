package com.example.cp3406_assessment_younghoocheon.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = F1Red,
    onPrimary = F1OnRed,
    primaryContainer = F1RedContainer,
    onPrimaryContainer = F1OnRedContainer
)

private val DarkColors = darkColorScheme(
    primary = F1Red,
    onPrimary = F1OnRed,
    primaryContainer = F1RedContainerDark,
    onPrimaryContainer = F1OnRedContainerDark,
    background = F1Black,
    onBackground = F1OnSurface,
    surface = F1Black,
    onSurface = F1OnSurface,
    surfaceVariant = F1SurfaceHigh,
    onSurfaceVariant = F1OnSurfaceVariant,
    surfaceContainerLowest = F1Black,
    surfaceContainerLow = F1SurfaceLow,
    surfaceContainer = F1Surface,
    surfaceContainerHigh = F1SurfaceHigh,
    surfaceContainerHighest = F1SurfaceHighest,
    outline = F1Outline,
    outlineVariant = F1OutlineVariant
)

/** Material 3 theme. Dark by default, like the F1 broadcast look the app is designed around. */
@Composable
fun AppTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}