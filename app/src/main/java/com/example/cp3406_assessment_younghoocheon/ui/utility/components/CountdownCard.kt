package com.example.cp3406_assessment_younghoocheon.ui.utility.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cp3406_assessment_younghoocheon.R
import com.example.cp3406_assessment_younghoocheon.data.model.RaceSession
import com.example.cp3406_assessment_younghoocheon.data.model.SessionType
import com.example.cp3406_assessment_younghoocheon.domain.CountdownParts
import com.example.cp3406_assessment_younghoocheon.domain.CountdownState
import com.example.cp3406_assessment_younghoocheon.ui.preview.PreviewData
import com.example.cp3406_assessment_younghoocheon.ui.theme.AppTheme
import com.example.cp3406_assessment_younghoocheon.ui.utility.labelRes

/** The big "at a glance" card: time left until the target session, or a hint when there is none. */
@Composable
fun CountdownCard(state: CountdownState, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (state) {
                is CountdownState.Upcoming -> UpcomingContent(state)
                is CountdownState.WeekendOver -> WeekendOverContent()
            }
        }
    }
}

/** Four countdown boxes (days, hours, minutes, seconds) read out as one sentence by screen readers. */
@Composable
private fun UpcomingContent(state: CountdownState.Upcoming) {
    val parts = state.remaining
    // Screen readers should hear one sentence instead of four separate numbers.
    val description = stringResource(
        R.string.countdown_content_description,
        parts.days,
        parts.hours,
        parts.minutes,
        parts.seconds
    )

    Text(
        text = stringResource(
            R.string.countdown_title,
            stringResource(state.session.type.labelRes())
        ),
        style = MaterialTheme.typography.titleMedium
    )
    Row(
        modifier = Modifier.clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CountdownUnit(value = parts.days.toString(), labelRes = R.string.countdown_unit_days)
        CountdownUnit(value = parts.hours.twoDigits(), labelRes = R.string.countdown_unit_hours)
        CountdownUnit(value = parts.minutes.twoDigits(), labelRes = R.string.countdown_unit_minutes)
        CountdownUnit(value = parts.seconds.twoDigits(), labelRes = R.string.countdown_unit_seconds)
    }
}

/** Shown when the race weekend has finished and no session is left to count down to. */
@Composable
private fun WeekendOverContent() {
    Text(
        text = stringResource(R.string.countdown_over_title),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
    Text(
        text = stringResource(R.string.countdown_over_message),
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center
    )
}

/** One number with its unit label underneath, e.g. "05" over "Hrs". */
@Composable
private fun CountdownUnit(value: String, @StringRes labelRes: Int) {
    Column(
        modifier = Modifier.widthIn(min = 56.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

/** Pads a count to two digits so the countdown does not jump in width ("5" becomes "05"). */
private fun Long.twoDigits(): String = toString().padStart(2, '0')

@Preview(showBackground = true)
@Composable
private fun CountdownCardUpcomingPreview() {
    AppTheme {
        CountdownCard(
            state = CountdownState.Upcoming(
                session = RaceSession(SessionType.QUALIFYING, PreviewData.now),
                remaining = CountdownParts(days = 2, hours = 5, minutes = 12, seconds = 33)
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CountdownCardOverPreview() {
    AppTheme {
        CountdownCard(
            state = CountdownState.WeekendOver,
            modifier = Modifier.padding(16.dp)
        )
    }
}