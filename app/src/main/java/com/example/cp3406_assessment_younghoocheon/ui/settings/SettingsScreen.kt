package com.example.cp3406_assessment_younghoocheon.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cp3406_assessment_younghoocheon.R
import com.example.cp3406_assessment_younghoocheon.domain.CountdownTargetMode
import com.example.cp3406_assessment_younghoocheon.domain.TimeZoneMode
import com.example.cp3406_assessment_younghoocheon.domain.UtilitySettings
import com.example.cp3406_assessment_younghoocheon.ui.theme.AppTheme

/**
 * Settings screen. Every control changes what the main screen shows.
 * The screen is stateless: it receives the current [settings] and reports edits via [onSettingsChange].
 */
@Composable
fun SettingsScreen(
    settings: UtilitySettings,
    onSettingsChange: (UtilitySettings) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SettingsSection(titleRes = R.string.settings_section_time) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OptionChip(
                    labelRes = R.string.settings_time_device,
                    selected = settings.timeZoneMode == TimeZoneMode.DEVICE,
                    onClick = { onSettingsChange(settings.copy(timeZoneMode = TimeZoneMode.DEVICE)) }
                )
                OptionChip(
                    labelRes = R.string.settings_time_utc,
                    selected = settings.timeZoneMode == TimeZoneMode.UTC,
                    onClick = { onSettingsChange(settings.copy(timeZoneMode = TimeZoneMode.UTC)) }
                )
            }
            SwitchRow(
                labelRes = R.string.settings_use_24h,
                checked = settings.use24Hour,
                onCheckedChange = { onSettingsChange(settings.copy(use24Hour = it)) }
            )
        }

        SettingsSection(titleRes = R.string.settings_section_countdown) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OptionChip(
                    labelRes = R.string.settings_target_next,
                    selected = settings.countdownTarget == CountdownTargetMode.NEXT_SESSION,
                    onClick = {
                        onSettingsChange(
                            settings.copy(countdownTarget = CountdownTargetMode.NEXT_SESSION)
                        )
                    }
                )
                OptionChip(
                    labelRes = R.string.settings_target_race,
                    selected = settings.countdownTarget == CountdownTargetMode.RACE,
                    onClick = {
                        onSettingsChange(settings.copy(countdownTarget = CountdownTargetMode.RACE))
                    }
                )
            }
        }

        SettingsSection(titleRes = R.string.settings_section_sessions) {
            SwitchRow(
                labelRes = R.string.settings_show_practice,
                checked = settings.showPractice,
                onCheckedChange = { onSettingsChange(settings.copy(showPractice = it)) }
            )
            SwitchRow(
                labelRes = R.string.settings_show_qualifying,
                checked = settings.showQualifying,
                onCheckedChange = { onSettingsChange(settings.copy(showQualifying = it)) }
            )
            SwitchRow(
                labelRes = R.string.settings_show_sprint,
                checked = settings.showSprint,
                onCheckedChange = { onSettingsChange(settings.copy(showSprint = it)) }
            )
            Text(
                text = stringResource(R.string.settings_race_always),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = stringResource(R.string.settings_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(onClick = { onSettingsChange(UtilitySettings()) }) {
            Text(text = stringResource(R.string.settings_reset))
        }
    }
}

/** Titled card that groups related settings. */
@Composable
private fun SettingsSection(
    @StringRes titleRes: Int,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleMedium
            )
            content()
        }
    }
}

/** Single-choice chip that shows a check mark while selected. */
@Composable
private fun OptionChip(
    @StringRes labelRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text = stringResource(labelRes)) },
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }
    )
}

/** Label plus switch; the whole row is one toggle target for easier tapping and accessibility. */
@Composable
private fun SwitchRow(
    @StringRes labelRes: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                role = Role.Switch,
                onValueChange = onCheckedChange
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        // onCheckedChange is null because the parent Row already handles the toggle.
        Switch(checked = checked, onCheckedChange = null)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreen(settings = UtilitySettings(), onSettingsChange = {})
    }
}
