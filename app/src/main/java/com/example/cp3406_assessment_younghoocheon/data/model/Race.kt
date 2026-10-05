package com.example.cp3406_assessment_younghoocheon.data.model

import java.time.Instant

/** Groups session types so that the settings screen can show or hide them together. */
enum class SessionCategory { PRACTICE, QUALIFYING, SPRINT, RACE }

/** Every kind of session that can appear in a race weekend. */
enum class SessionType(val category: SessionCategory) {
    FIRST_PRACTICE(SessionCategory.PRACTICE),
    SECOND_PRACTICE(SessionCategory.PRACTICE),
    THIRD_PRACTICE(SessionCategory.PRACTICE),
    SPRINT_QUALIFYING(SessionCategory.QUALIFYING),
    SPRINT(SessionCategory.SPRINT),
    QUALIFYING(SessionCategory.QUALIFYING),
    RACE(SessionCategory.RACE)
}

/** A single session and the exact moment it starts. */
data class RaceSession(
    val type: SessionType,
    val startTime: Instant
)

/**
 * UI-facing model of a race weekend.
 *
 * [sessions] is always sorted by start time and always contains the race itself.
 */
data class Race(
    val season: String,
    val round: Int,
    val name: String,
    val circuitName: String,
    val locality: String,
    val country: String,
    val sessions: List<RaceSession>
)

/** "Circuit, City, Country" with blank parts left out. */
fun Race.locationLabel(): String =
    listOf(circuitName, locality, country)
        .filter { it.isNotBlank() }
        .joinToString(separator = ", ")
