package com.example.cp3406_assessment_younghoocheon.data.mapper

import com.example.cp3406_assessment_younghoocheon.data.model.Race
import com.example.cp3406_assessment_younghoocheon.data.model.RaceSession
import com.example.cp3406_assessment_younghoocheon.data.model.SessionType
import com.example.cp3406_assessment_younghoocheon.data.remote.RaceDto
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeParseException

private const val DEFAULT_RACE_NAME = "Grand Prix"

/**
 * Converts the network DTO into the [Race] model used by the rest of the app.
 *
 * @throws IllegalArgumentException if the race itself has no usable start time.
 */
fun RaceDto.toDomain(): Race {
    val raceStart = parseInstant(date, time)
        ?: throw IllegalArgumentException("Race has no valid start time")

    val optionalSessions = listOf(
        SessionType.FIRST_PRACTICE to firstPractice,
        SessionType.SECOND_PRACTICE to secondPractice,
        SessionType.THIRD_PRACTICE to thirdPractice,
        // Older seasons call the sprint qualifying session "SprintShootout".
        SessionType.SPRINT_QUALIFYING to (sprintQualifying ?: sprintShootout),
        SessionType.SPRINT to sprint,
        SessionType.QUALIFYING to qualifying
    )

    val sessions = optionalSessions
        .mapNotNull { (type, dto) ->
            parseInstant(dto?.date, dto?.time)?.let { RaceSession(type, it) }
        }
        .plus(RaceSession(SessionType.RACE, raceStart))
        .sortedBy { it.startTime }

    return Race(
        season = season.orEmpty(),
        round = round?.toIntOrNull() ?: 0,
        name = raceName?.takeIf { it.isNotBlank() } ?: DEFAULT_RACE_NAME,
        circuitName = circuit?.circuitName.orEmpty(),
        locality = circuit?.location?.locality.orEmpty(),
        country = circuit?.location?.country.orEmpty(),
        sessions = sessions
    )
}

/**
 * Combines an ISO date ("2025-05-04") and an optional UTC time ("20:00:00Z") into an [Instant].
 * A missing time falls back to midnight UTC; unparsable input returns null.
 */
internal fun parseInstant(date: String?, time: String?): Instant? {
    if (date.isNullOrBlank()) return null
    return try {
        if (time.isNullOrBlank()) {
            LocalDate.parse(date).atStartOfDay(ZoneOffset.UTC).toInstant()
        } else {
            val utcTime = if (time.endsWith("Z")) time else "${time}Z"
            Instant.parse("${date}T$utcTime")
        }
    } catch (e: DateTimeParseException) {
        null
    }
}
