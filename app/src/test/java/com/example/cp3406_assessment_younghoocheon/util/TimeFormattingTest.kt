package com.example.cp3406_assessment_younghoocheon.util

import com.example.cp3406_assessment_younghoocheon.domain.TimeZoneMode
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeFormattingTest {

    private val instant = Instant.parse("2025-05-04T14:05:00Z")

    @Test
    fun time24h_inUtc() {
        assertEquals(
            "14:05",
            formatSessionTime(instant, ZoneId.of("UTC"), use24Hour = true, locale = Locale.ENGLISH)
        )
    }

    @Test
    fun time24h_isConvertedToTheRequestedZone() {
        assertEquals(
            "22:05",
            formatSessionTime(instant, ZoneId.of("Asia/Singapore"), use24Hour = true, locale = Locale.ENGLISH)
        )
    }

    @Test
    fun day_isFormattedInTheRequestedZone() {
        assertEquals(
            "Sun 4 May",
            formatSessionDay(instant, ZoneId.of("UTC"), locale = Locale.ENGLISH)
        )
        // 14:05 UTC is already the next day in New Zealand (UTC+12 in May).
        assertEquals(
            "Mon 5 May",
            formatSessionDay(instant, ZoneId.of("Pacific/Auckland"), locale = Locale.ENGLISH)
        )
    }

    @Test
    fun utcMode_alwaysGivesUtcZone() {
        assertEquals("UTC", zoneFor(TimeZoneMode.UTC).id)
    }
}
