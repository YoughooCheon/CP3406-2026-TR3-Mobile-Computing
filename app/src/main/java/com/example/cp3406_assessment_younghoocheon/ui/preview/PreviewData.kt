package com.example.cp3406_assessment_younghoocheon.ui.preview

import com.example.cp3406_assessment_younghoocheon.data.model.Race
import com.example.cp3406_assessment_younghoocheon.data.model.RaceSession
import com.example.cp3406_assessment_younghoocheon.data.model.SessionType
import java.time.Instant

/** Fixed sample data used only by @Preview functions. */
object PreviewData {

    /** A moment during the week before the sample race. */
    val now: Instant = Instant.parse("2025-11-27T09:00:00Z")

    val race = Race(
        season = "2025",
        round = 23,
        name = "Qatar Grand Prix",
        circuitName = "Losail International Circuit",
        locality = "Al Daayen",
        country = "Qatar",
        sessions = listOf(
            RaceSession(SessionType.FIRST_PRACTICE, Instant.parse("2025-11-28T13:30:00Z")),
            RaceSession(SessionType.SPRINT_QUALIFYING, Instant.parse("2025-11-28T17:30:00Z")),
            RaceSession(SessionType.SPRINT, Instant.parse("2025-11-29T14:00:00Z")),
            RaceSession(SessionType.QUALIFYING, Instant.parse("2025-11-29T18:00:00Z")),
            RaceSession(SessionType.RACE, Instant.parse("2025-11-30T16:00:00Z"))
        )
    )
}
