package com.example.cp3406_assessment_younghoocheon.data.repository

import com.example.cp3406_assessment_younghoocheon.data.model.Race

/** Reasons why loading the next race can fail. The UI maps each one to a friendly message. */
enum class RaceError { NO_RACE, NETWORK, SERVER, DATA }

/** Failure carrying a [RaceError] so upper layers never see Retrofit or OkHttp exceptions. */
class RaceLoadException(
    val error: RaceError,
    cause: Throwable? = null
) : Exception("Could not load race: $error", cause)

/** Single source of truth for race data. */
interface RaceRepository {

    /** Loads the next upcoming race; failures are wrapped in [RaceLoadException]. */
    suspend fun getNextRace(): Result<Race>
}
