package com.example.cp3406_assessment_younghoocheon.ui.utility

import androidx.annotation.StringRes
import com.example.cp3406_assessment_younghoocheon.R
import com.example.cp3406_assessment_younghoocheon.data.model.SessionType

/** Localised display name of a session type. */
@StringRes
fun SessionType.labelRes(): Int = when (this) {
    SessionType.FIRST_PRACTICE -> R.string.session_first_practice
    SessionType.SECOND_PRACTICE -> R.string.session_second_practice
    SessionType.THIRD_PRACTICE -> R.string.session_third_practice
    SessionType.SPRINT_QUALIFYING -> R.string.session_sprint_qualifying
    SessionType.SPRINT -> R.string.session_sprint
    SessionType.QUALIFYING -> R.string.session_qualifying
    SessionType.RACE -> R.string.session_race
}
