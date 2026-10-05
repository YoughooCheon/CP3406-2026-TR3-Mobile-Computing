package com.example.cp3406_assessment_younghoocheon.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cp3406_assessment_younghoocheon.R
import com.example.cp3406_assessment_younghoocheon.ui.settings.SettingsScreen
import com.example.cp3406_assessment_younghoocheon.ui.theme.F1RedBright
import com.example.cp3406_assessment_younghoocheon.ui.utility.UtilityScreen
import com.example.cp3406_assessment_younghoocheon.ui.utility.UtilityViewModel

/**
 * App scaffold: top bar, bottom navigation and the currently selected screen.
 *
 * A single [UtilityViewModel] is shared by all screens so that changing a setting
 * immediately changes what the home screen shows.
 */
@Composable
fun UtilityApp(
    viewModel: UtilityViewModel = viewModel(factory = UtilityViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    // Kept as a State (not unwrapped) so only the composables that read it recompose each second.
    val now = viewModel.now.collectAsStateWithLifecycle()
    var currentDestination by rememberSaveable { mutableStateOf(AppDestination.HOME) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = stringResource(currentDestination.titleRes),
                showRefresh = currentDestination == AppDestination.HOME,
                onRefresh = viewModel::refresh
            )
        },
        bottomBar = {
            AppBottomBar(
                currentDestination = currentDestination,
                onDestinationSelected = { currentDestination = it }
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (currentDestination) {
            AppDestination.HOME -> UtilityScreen(
                uiState = uiState,
                settings = settings,
                now = now,
                onRetry = viewModel::refresh,
                modifier = contentModifier
            )

            AppDestination.SETTINGS -> SettingsScreen(
                settings = settings,
                onSettingsChange = viewModel::updateSettings,
                modifier = contentModifier
            )
        }
    }
}

/** Left-aligned F1 logo + screen title, with a refresh action on the home screen. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar(
    title: String,
    showRefresh: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Decorative: the title next to it already names the screen.
                Image(
                    painter = painterResource(R.drawable.f1_logo),
                    contentDescription = null,
                    modifier = Modifier.height(18.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = title, fontWeight = FontWeight.SemiBold)
            }
        },
        actions = {
            if (showRefresh) {
                IconButton(onClick = onRefresh) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = stringResource(R.string.action_refresh)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

/** Two-tab navigation bar; the selected tab is tinted F1 red. */
@Composable
private fun AppBottomBar(
    currentDestination: AppDestination,
    onDestinationSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        AppDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = destination == currentDestination,
                onClick = { onDestinationSelected(destination) },
                icon = { Icon(imageVector = destination.icon, contentDescription = null) },
                label = { Text(text = stringResource(destination.labelRes)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = F1RedBright,
                    selectedTextColor = F1RedBright,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}