package com.example.cp3406_assessment_younghoocheon.data

import com.example.cp3406_assessment_younghoocheon.data.mapper.parseInstant
import com.example.cp3406_assessment_younghoocheon.data.mapper.toDomain
import com.example.cp3406_assessment_younghoocheon.data.model.SessionType
import com.example.cp3406_assessment_younghoocheon.data.remote.CircuitDto
import com.example.cp3406_assessment_younghoocheon.data.remote.LocationDto
import com.example.cp3406_assessment_younghoocheon.data.remote.RaceDto
import com.example.cp3406_assessment_younghoocheon.data.remote.SessionDto
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Test

class RaceMapperTest {

    private val circuit = CircuitDto(
        circuitName = "Losail International Circuit",
        location = LocationDto(locality = "Al Daayen", country = "Qatar")
    )

    @Test
    fun standardWeekend_isMappedAndSortedByStartTime() {
        val dto = RaceDto(
            season = "2025",
            round = "5",
            raceName = "Miami Grand Prix",
            circuit = circuit,
            date = "2025-05-04",
            time = "20:00:00Z",
            firstPractice = SessionDto("2025-05-02", "16:30:00Z"),
            secondPractice = SessionDto("2025-05-02", "20:00:00Z"),
            thirdPractice = SessionDto("2025-05-03", "16:30:00Z"),
            qualifying = SessionDto("2025-05-03", "20:00:00Z")
        )

        val race = dto.toDomain()

        assertEquals("Miami Grand Prix", race.name)
        assertEquals(5, race.round)
        assertEquals("2025", race.season)
        assertEquals("Al Daayen", race.locality)
        assertEquals("Qatar", race.country)
        assertEquals(
            listOf(
                SessionType.FIRST_PRACTICE,
                SessionType.SECOND_PRACTICE,
                SessionType.THIRD_PRACTICE,
                SessionType.QUALIFYING,
                SessionType.RACE
            ),
            race.sessions.map { it.type }
        )
        assertEquals(Instant.parse("2025-05-04T20:00:00Z"), race.sessions.last().startTime)
    }

    @Test
    fun sprintWeekend_hasSprintSessionsInChronologicalOrder() {
        val dto = RaceDto(
            season = "2024",
            round = "23",
            raceName = "Qatar Grand Prix",
            circuit = circuit,
            date = "2024-12-01",
            time = "16:00:00Z",
            firstPractice = SessionDto("2024-11-29", "13:30:00Z"),
            sprintQualifying = SessionDto("2024-11-29", "17:30:00Z"),
            sprint = SessionDto("2024-11-30", "14:00:00Z"),
            qualifying = SessionDto("2024-11-30", "18:00:00Z")
        )

        val types = dto.toDomain().sessions.map { it.type }

        assertEquals(
            listOf(
                SessionType.FIRST_PRACTICE,
                SessionType.SPRINT_QUALIFYING,
                SessionType.SPRINT,
                SessionType.QUALIFYING,
                SessionType.RACE
            ),
            types
        )
    }

    @Test
    fun legacySprintShootout_isTreatedAsSprintQualifying() {
        val dto = RaceDto(
            date = "2023-07-30",
            time = "13:00:00Z",
            sprintShootout = SessionDto("2023-07-28", "15:00:00Z")
        )

        val types = dto.toDomain().sessions.map { it.type }

        assertEquals(listOf(SessionType.SPRINT_QUALIFYING, SessionType.RACE), types)
    }

    @Test
    fun missingTime_fallsBackToMidnightUtc() {
        assertEquals(
            Instant.parse("2025-05-04T00:00:00Z"),
            parseInstant("2025-05-04", null)
        )
    }

    @Test
    fun invalidOrMissingDate_returnsNull() {
        assertNull(parseInstant(null, "20:00:00Z"))
        assertNull(parseInstant("not-a-date", "20:00:00Z"))
    }

    @Test
    fun raceWithoutStartTime_isRejected() {
        try {
            RaceDto(raceName = "Broken GP").toDomain()
            fail("Expected IllegalArgumentException")
        } catch (expected: IllegalArgumentException) {
            // expected
        }
    }

    @Test
    fun missingOptionalFields_useSafeDefaults() {
        val race = RaceDto(date = "2025-05-04", time = "20:00:00Z").toDomain()

        assertEquals("Grand Prix", race.name)
        assertEquals(0, race.round)
        assertEquals("", race.circuitName)
        assertEquals(1, race.sessions.size)
    }
}
