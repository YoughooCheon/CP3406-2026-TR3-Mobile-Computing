package com.example.cp3406_assessment_younghoocheon.ui.preview

import com.example.cp3406_assessment_younghoocheon.data.model.NewsArticle
import com.example.cp3406_assessment_younghoocheon.data.model.Race
import com.example.cp3406_assessment_younghoocheon.data.model.RaceSession
import com.example.cp3406_assessment_younghoocheon.data.model.SessionType
import java.time.Instant

/** Fixed sample data used only by @Preview functions. */
object PreviewData {

    /** A moment during the week before the sample race. */
    val now: Instant = Instant.parse("2025-11-27T09:00:00Z")

    val news = listOf(
        NewsArticle(
            title = "Can Red Bull keep up their momentum after Verstappen's Sepang win?",
            summary = "Max Verstappen has been in immense form all year, and the Sepang win proved he now has a car to match.",
            link = "https://www.formula1.com/en/latest"
        ),
        NewsArticle(
            title = "Who impressed our judges at the Bahrain GP in Malaysia?",
            summary = "Power Rankings after the race.",
            link = "https://www.formula1.com/en/latest"
        ),
        NewsArticle(
            title = "What time is the 2026 Singapore GP and how can I watch it?",
            summary = "All the timings for the weekend.",
            link = "https://www.formula1.com/en/latest"
        )
    )

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