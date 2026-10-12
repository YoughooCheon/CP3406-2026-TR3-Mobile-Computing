package com.example.cp3406_assessment_younghoocheon.ui.utility.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cp3406_assessment_younghoocheon.R
import com.example.cp3406_assessment_younghoocheon.data.model.Race
import com.example.cp3406_assessment_younghoocheon.domain.CountdownState
import com.example.cp3406_assessment_younghoocheon.domain.UtilitySettings
import com.example.cp3406_assessment_younghoocheon.domain.computeCountdown
import com.example.cp3406_assessment_younghoocheon.domain.visibleSessions
import com.example.cp3406_assessment_younghoocheon.ui.preview.PreviewData
import com.example.cp3406_assessment_younghoocheon.ui.theme.AppTheme
import com.example.cp3406_assessment_younghoocheon.ui.theme.F1CardBorder
import com.example.cp3406_assessment_younghoocheon.util.zoneFor
import java.time.Instant

/**
 * Weekend timetable. Past sessions are dimmed and the countdown target is highlighted.
 * [now] is read here (and not by the parent) so only this card recomposes every second.
 */
@Composable
fun SessionScheduleCard(
    race: Race,
    settings: UtilitySettings,
    now: State<Instant>,
    modifier: Modifier = Modifier
) {
    val zone = remember(settings.timeZoneMode) { zoneFor(settings.timeZoneMode) }
    val sessions = remember(race, settings) { visibleSessions(race, settings) }

    val currentTime = now.value
    val target = (computeCountdown(race, settings, currentTime) as? CountdownState.Upcoming)?.session

    Card(modifier = modifier.fillMaxWidth(), border = F1CardBorder) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = stringResource(R.string.schedule_title),
                style = MaterialTheme.typography.titleMedium
            )
            sessions.forEachIndexed { index, session ->
                if (index > 0) HorizontalDivider()
                val status = when {
                    !session.startTime.isAfter(currentTime) -> SessionStatus.PAST
                    session == target -> SessionStatus.NEXT
                    else -> SessionStatus.UPCOMING
                }
                SessionRow(
                    session = session,
                    status = status,
                    zone = zone,
                    use24Hour = settings.use24Hour
                )
            }
            Text(
                text = stringResource(R.string.schedule_times_in, zone.id),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SessionScheduleCardPreview() {
    AppTheme {
        SessionScheduleCard(
            race = PreviewData.race,
            settings = UtilitySettings(),
            now = remember { mutableStateOf(PreviewData.now) },
            modifier = Modifier.padding(16.dp)
        )
    }
}