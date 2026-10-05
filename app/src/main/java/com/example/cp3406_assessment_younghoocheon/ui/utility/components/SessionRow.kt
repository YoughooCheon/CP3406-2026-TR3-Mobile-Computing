package com.example.cp3406_assessment_younghoocheon.ui.utility.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cp3406_assessment_younghoocheon.R
import com.example.cp3406_assessment_younghoocheon.data.model.RaceSession
import com.example.cp3406_assessment_younghoocheon.data.model.SessionType
import com.example.cp3406_assessment_younghoocheon.ui.preview.PreviewData
import com.example.cp3406_assessment_younghoocheon.ui.theme.AppTheme
import com.example.cp3406_assessment_younghoocheon.ui.utility.labelRes
import com.example.cp3406_assessment_younghoocheon.util.formatSessionDay
import com.example.cp3406_assessment_younghoocheon.util.formatSessionTime
import java.time.ZoneId

/** Where a session sits relative to "now". */
enum class SessionStatus { PAST, NEXT, UPCOMING }

/** One line of the weekend schedule: name and day on the left, start time on the right. */
@Composable
fun SessionRow(
    session: RaceSession,
    status: SessionStatus,
    zone: ZoneId,
    use24Hour: Boolean,
    modifier: Modifier = Modifier
) {
    val day = remember(session, zone) { formatSessionDay(session.startTime, zone) }
    val time = remember(session, zone, use24Hour) {
        formatSessionTime(session.startTime, zone, use24Hour)
    }
    val contentColor = when (status) {
        SessionStatus.PAST -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        SessionStatus.NEXT -> MaterialTheme.colorScheme.primary
        SessionStatus.UPCOMING -> MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(session.type.labelRes()),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (status == SessionStatus.NEXT) FontWeight.Bold else FontWeight.Normal,
                color = contentColor
            )
            Text(
                text = day,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor
            )
        }
        if (status == SessionStatus.NEXT) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = stringResource(R.string.schedule_badge_next),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = time,
            style = MaterialTheme.typography.titleLarge,
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SessionRowPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            val session = RaceSession(SessionType.QUALIFYING, PreviewData.now)
            SessionRow(session, SessionStatus.PAST, ZoneId.of("UTC"), use24Hour = true)
            SessionRow(session, SessionStatus.NEXT, ZoneId.of("UTC"), use24Hour = true)
            SessionRow(session, SessionStatus.UPCOMING, ZoneId.of("UTC"), use24Hour = false)
        }
    }
}
