package com.example.cp3406_assessment_younghoocheon.ui.utility.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.cp3406_assessment_younghoocheon.ui.theme.F1CountdownEnd
import com.example.cp3406_assessment_younghoocheon.ui.theme.F1CountdownStart
import com.example.cp3406_assessment_younghoocheon.ui.theme.F1Red
import com.example.cp3406_assessment_younghoocheon.ui.utility.labelRes

/** The big "at a glance" card (red-to-black gradient): time left until the target session, or a hint when there is none. */
@Composable
fun CountdownCard(state: CountdownState, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        border = BorderStroke(width = 1.dp, color = F1Red.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // Red fading into black, like the F1 broadcast graphics.
                .background(
                    Brush.linearGradient(listOf(F1CountdownStart, F1CountdownEnd))
                )
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
        modifier = Modifier
            .fillMaxWidth()
            .clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // weight(1f): the four boxes share the width equally, so they fit small phones too.
        CountdownUnit(
            value = parts.days.toString(),
            labelRes = R.string.countdown_unit_days,
            modifier = Modifier.weight(1f)
        )
        CountdownUnit(
            value = parts.hours.twoDigits(),
            labelRes = R.string.countdown_unit_hours,
            modifier = Modifier.weight(1f)
        )
        CountdownUnit(
            value = parts.minutes.twoDigits(),
            labelRes = R.string.countdown_unit_minutes,
            modifier = Modifier.weight(1f)
        )
        CountdownUnit(
            value = parts.seconds.twoDigits(),
            labelRes = R.string.countdown_unit_seconds,
            modifier = Modifier.weight(1f)
        )
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

/** One number in a dark box with its unit label underneath, e.g. "05" over "Hrs". */
@Composable
private fun CountdownUnit(
    value: String,
    @StringRes labelRes: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = Color.Black.copy(alpha = 0.55f)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color.White
            )
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.75f)
            )
        }
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