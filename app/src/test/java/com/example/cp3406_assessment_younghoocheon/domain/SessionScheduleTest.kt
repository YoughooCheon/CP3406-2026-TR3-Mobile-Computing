package com.example.cp3406_assessment_younghoocheon.domain

import com.example.cp3406_assessment_younghoocheon.data.model.Race
import com.example.cp3406_assessment_younghoocheon.data.model.RaceSession
import com.example.cp3406_assessment_younghoocheon.data.model.SessionType
import java.time.Duration
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Test

class SessionScheduleTest {

    private fun at(text: String): Instant = Instant.parse(text)

    /** Fri 10:00 FP1, Fri 14:00 FP2, Sat 10:00 FP3, Sat 14:00 Quali, Sun 14:00 Race (all UTC). */
    private val race = Race(
        season = "2025",
        round = 1,
        name = "Test Grand Prix",
        circuitName = "Test Circuit",
        locality = "Testville",
        country = "Testland",
        sessions = listOf(
            RaceSession(SessionType.FIRST_PRACTICE, at("2025-05-02T10:00:00Z")),
            RaceSession(SessionType.SECOND_PRACTICE, at("2025-05-02T14:00:00Z")),
            RaceSession(SessionType.THIRD_PRACTICE, at("2025-05-03T10:00:00Z")),
            RaceSession(SessionType.QUALIFYING, at("2025-05-03T14:00:00Z")),
            RaceSession(SessionType.RACE, at("2025-05-04T14:00:00Z"))
        )
    )

    @Test
    fun durationIsSplitIntoDaysHoursMinutesSeconds() {
        val parts = Duration.ofSeconds(90_061).toCountdownParts()

        assertEquals(CountdownParts(days = 1, hours = 1, minutes = 1, seconds = 1), parts)
    }

    @Test
    fun negativeDurationBecomesZero() {
        val parts = Duration.ofSeconds(-30).toCountdownParts()

        assertEquals(CountdownParts(0, 0, 0, 0), parts)
    }

    @Test
    fun nextSessionMode_countsDownToFirstUpcomingSession() {
        val state = computeCountdown(race, UtilitySettings(), at("2025-05-02T12:00:00Z"))

        val upcoming = state as CountdownState.Upcoming
        assertEquals(SessionType.SECOND_PRACTICE, upcoming.session.type)
        assertEquals(CountdownParts(0, 2, 0, 0), upcoming.remaining)
    }

    @Test
    fun raceMode_countsDownToTheRace() {
        val settings = UtilitySettings(countdownTarget = CountdownTargetMode.RACE)

        val state = computeCountdown(race, settings, at("2025-05-02T12:00:00Z"))

        val upcoming = state as CountdownState.Upcoming
        assertEquals(SessionType.RACE, upcoming.session.type)
        assertEquals(CountdownParts(days = 2, hours = 2, minutes = 0, seconds = 0), upcoming.remaining)
    }

    @Test
    fun hiddenSessionsAreSkippedWhenPickingTheTarget() {
        val settings = UtilitySettings(showPractice = false)

        val state = computeCountdown(race, settings, at("2025-05-02T09:00:00Z"))

        val upcoming = state as CountdownState.Upcoming
        assertEquals(SessionType.QUALIFYING, upcoming.session.type)
    }

    @Test
    fun afterTheRace_weekendIsOver() {
        val state = computeCountdown(race, UtilitySettings(), at("2025-05-04T15:00:00Z"))

        assertEquals(CountdownState.WeekendOver, state)
    }

    @Test
    fun raceModeAfterRaceStart_weekendIsOver() {
        val settings = UtilitySettings(countdownTarget = CountdownTargetMode.RACE)

        val state = computeCountdown(race, settings, at("2025-05-04T14:00:00Z"))

        assertEquals(CountdownState.WeekendOver, state)
    }

    @Test
    fun raceIsAlwaysVisible_evenWhenEverythingElseIsHidden() {
        val settings = UtilitySettings(
            showPractice = false,
            showQualifying = false,
            showSprint = false
        )

        val types = visibleSessions(race, settings).map { it.type }

        assertEquals(listOf(SessionType.RACE), types)
    }
}
