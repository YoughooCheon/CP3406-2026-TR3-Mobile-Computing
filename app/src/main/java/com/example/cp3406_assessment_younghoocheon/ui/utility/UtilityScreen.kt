package com.example.cp3406_assessment_younghoocheon.ui.utility

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cp3406_assessment_younghoocheon.data.model.Race
import com.example.cp3406_assessment_younghoocheon.domain.UtilitySettings
import com.example.cp3406_assessment_younghoocheon.domain.computeCountdown
import com.example.cp3406_assessment_younghoocheon.ui.preview.PreviewData
import com.example.cp3406_assessment_younghoocheon.ui.theme.AppTheme
import com.example.cp3406_assessment_younghoocheon.ui.utility.components.CountdownCard
import com.example.cp3406_assessment_younghoocheon.ui.utility.components.ErrorContent
import com.example.cp3406_assessment_younghoocheon.ui.utility.components.LoadingContent
import com.example.cp3406_assessment_younghoocheon.ui.utility.components.NewsSection
import com.example.cp3406_assessment_younghoocheon.ui.utility.components.RaceHeader
import com.example.cp3406_assessment_younghoocheon.ui.utility.components.SessionScheduleCard
import java.time.Instant

/**
 * Main "at a glance" screen: next race, live countdown, latest F1 news and the weekend timetable.
 *
 * All state is hoisted: this composable only renders what it is given.
 */
@Composable
fun UtilityScreen(
    uiState: UtilityUiState,
    settings: UtilitySettings,
    now: State<Instant>,
    newsState: NewsUiState,
    onRetry: () -> Unit,
    onRetryNews: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is UtilityUiState.Loading -> LoadingContent(modifier = modifier)
        is UtilityUiState.Error -> ErrorContent(
            error = uiState.error,
            onRetry = onRetry,
            modifier = modifier
        )
        is UtilityUiState.Success -> RaceContent(
            race = uiState.race,
            settings = settings,
            now = now,
            newsState = newsState,
            onRetryNews = onRetryNews,
            modifier = modifier
        )
    }
}

/** Scrollable success state: header, live countdown, latest news and weekend timetable. */
@Composable
private fun RaceContent(
    race: Race,
    settings: UtilitySettings,
    now: State<Instant>,
    newsState: NewsUiState,
    onRetryNews: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RaceHeader(race = race)
        CountdownSection(race = race, settings = settings, now = now)
        NewsSection(state = newsState, onRetry = onRetryNews)
        SessionScheduleCard(race = race, settings = settings, now = now)
    }
}

/** Reads [now] in its own scope so only the countdown recomposes every second. */
@Composable
private fun CountdownSection(
    race: Race,
    settings: UtilitySettings,
    now: State<Instant>
) {
    CountdownCard(state = computeCountdown(race, settings, now.value))
}

@Preview(showBackground = true)
@Composable
private fun UtilityScreenPreview() {
    AppTheme {
        UtilityScreen(
            uiState = UtilityUiState.Success(PreviewData.race),
            settings = UtilitySettings(),
            now = remember { mutableStateOf(PreviewData.now) },
            newsState = NewsUiState.Success(PreviewData.news),
            onRetry = {},
            onRetryNews = {}
        )
    }
}