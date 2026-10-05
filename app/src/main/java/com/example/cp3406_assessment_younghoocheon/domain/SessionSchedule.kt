package com.example.cp3406_assessment_younghoocheon.domain

import com.example.cp3406_assessment_younghoocheon.data.model.Race
import com.example.cp3406_assessment_younghoocheon.data.model.RaceSession
import com.example.cp3406_assessment_younghoocheon.data.model.SessionType
import java.time.Duration
import java.time.Instant

private const val SECONDS_PER_MINUTE = 60L
private const val SECONDS_PER_HOUR = 3_600L
private const val SECONDS_PER_DAY = 86_400L

/** A remaining duration split into the units shown on the countdown card. */
data class CountdownParts(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long
)

/** Result of asking "what are we counting down to right now?". */
sealed interface CountdownState {

    /** [session] has not started yet; [remaining] is the time left. */
    data class Upcoming(
        val session: RaceSession,
        val remaining: CountdownParts
    ) : CountdownState

    /** Nothing left to count down to (the weekend is under way or finished). */
    data object WeekendOver : CountdownState
}

/** Splits a [Duration] into days, hours, minutes and seconds (negative durations become zero). */
fun Duration.toCountdownParts(): CountdownParts {
    val total = seconds.coerceAtLeast(0L)
    return CountdownParts(
        days = total / SECONDS_PER_DAY,
        hours = (total % SECONDS_PER_DAY) / SECONDS_PER_HOUR,
        minutes = (total % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE,
        seconds = total % SECONDS_PER_MINUTE
    )
}

/** Sessions the user wants to see, in chronological order. */
fun visibleSessions(race: Race, settings: UtilitySettings): List<RaceSession> =
    race.sessions.filter { settings.shows(it.type.category) }

/** Works out the countdown for [race] at time [now] according to the user's [settings]. */
fun computeCountdown(race: Race, settings: UtilitySettings, now: Instant): CountdownState {
    val target: RaceSession? = when (settings.countdownTarget) {
        CountdownTargetMode.RACE ->
            race.sessions.firstOrNull { it.type == SessionType.RACE }

        CountdownTargetMode.NEXT_SESSION ->
            visibleSessions(race, settings).firstOrNull { it.startTime.isAfter(now) }
    }

    return if (target != null && target.startTime.isAfter(now)) {
        CountdownState.Upcoming(
            session = target,
            remaining = Duration.between(now, target.startTime).toCountdownParts()
        )
    } else {
        CountdownState.WeekendOver
    }
}
