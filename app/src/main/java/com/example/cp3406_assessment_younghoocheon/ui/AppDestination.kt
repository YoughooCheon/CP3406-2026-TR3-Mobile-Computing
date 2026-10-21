package com.example.cp3406_assessment_younghoocheon.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.cp3406_assessment_younghoocheon.R

/** The two bottom-navigation destinations of the app. */
enum class AppDestination(
    @StringRes val titleRes: Int,
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    HOME(R.string.title_race, R.string.nav_home, Icons.Filled.Home),

    SETTINGS(R.string.title_settings, R.string.nav_settings, Icons.Filled.Settings)
}