package com.example.cp3406_assessment_younghoocheon.util

import com.example.cp3406_assessment_younghoocheon.domain.TimeZoneMode
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val DAY_PATTERN = "EEE d MMM"
private const val TIME_PATTERN_24H = "HH:mm"
private const val TIME_PATTERN_12H = "h:mm a"

/** Time zone that matches the user's [TimeZoneMode] choice. */
fun zoneFor(mode: TimeZoneMode): ZoneId = when (mode) {
    TimeZoneMode.DEVICE -> ZoneId.systemDefault()
    TimeZoneMode.UTC -> ZoneId.of("UTC")
}

/** e.g. "Sat 30 Nov". */
fun formatSessionDay(
    instant: Instant,
    zone: ZoneId,
    locale: Locale = Locale.getDefault()
): String = DateTimeFormatter.ofPattern(DAY_PATTERN, locale).withZone(zone).format(instant)

/** e.g. "17:30" or "5:30 PM". */
fun formatSessionTime(
    instant: Instant,
    zone: ZoneId,
    use24Hour: Boolean,
    locale: Locale = Locale.getDefault()
): String {
    val pattern = if (use24Hour) TIME_PATTERN_24H else TIME_PATTERN_12H
    return DateTimeFormatter.ofPattern(pattern, locale).withZone(zone).format(instant)
}
