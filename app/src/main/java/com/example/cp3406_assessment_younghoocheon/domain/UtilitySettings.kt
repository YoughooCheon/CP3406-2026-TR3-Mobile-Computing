package com.example.cp3406_assessment_younghoocheon.domain

import com.example.cp3406_assessment_younghoocheon.data.model.SessionCategory

/** Which time zone session times are shown in. */
enum class TimeZoneMode { DEVICE, UTC }

/** What the big countdown counts down to. */
enum class CountdownTargetMode { NEXT_SESSION, RACE }

/**
 * User preferences that control the content of the main screen.
 * They live in memory only (the assignment does not require persistence).
 */
data class UtilitySettings(
    val timeZoneMode: TimeZoneMode = TimeZoneMode.DEVICE,
    val use24Hour: Boolean = true,
    val countdownTarget: CountdownTargetMode = CountdownTargetMode.NEXT_SESSION,
    val showPractice: Boolean = true,
    val showQualifying: Boolean = true,
    val showSprint: Boolean = true
) {
    /** The race itself can never be hidden. */
    fun shows(category: SessionCategory): Boolean = when (category) {
        SessionCategory.PRACTICE -> showPractice
        SessionCategory.QUALIFYING -> showQualifying
        SessionCategory.SPRINT -> showSprint
        SessionCategory.RACE -> true
    }
}
