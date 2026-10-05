package com.example.cp3406_assessment_younghoocheon.data.remote

import com.google.gson.annotations.SerializedName

/*
 * Data transfer objects for the Jolpica F1 (Ergast-compatible) "next race" endpoint.
 * Every field is nullable with a default so a missing key in the JSON never crashes parsing;
 * the mapper decides what is mandatory.
 */

/** Root of the JSON response. */
data class NextRaceResponse(
    @SerializedName("MRData") val mrData: MrDataDto? = null
)

/** Wrapper object that Jolpica puts around every response. */
data class MrDataDto(
    @SerializedName("RaceTable") val raceTable: RaceTableDto? = null
)

/** Holds the list of races; the "next race" endpoint returns at most one entry. */
data class RaceTableDto(
    @SerializedName("Races") val races: List<RaceDto>? = null
)

/** One race weekend. Sprint weekends omit the second and third practice sessions. */
data class RaceDto(
    @SerializedName("season") val season: String? = null,
    @SerializedName("round") val round: String? = null,
    @SerializedName("raceName") val raceName: String? = null,
    @SerializedName("Circuit") val circuit: CircuitDto? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("time") val time: String? = null,
    @SerializedName("FirstPractice") val firstPractice: SessionDto? = null,
    @SerializedName("SecondPractice") val secondPractice: SessionDto? = null,
    @SerializedName("ThirdPractice") val thirdPractice: SessionDto? = null,
    @SerializedName("Qualifying") val qualifying: SessionDto? = null,
    @SerializedName("Sprint") val sprint: SessionDto? = null,
    @SerializedName("SprintQualifying") val sprintQualifying: SessionDto? = null,
    @SerializedName("SprintShootout") val sprintShootout: SessionDto? = null
)

/** Date (yyyy-MM-dd) and optional UTC time (HH:mm:ssZ) of a single session. */
data class SessionDto(
    @SerializedName("date") val date: String? = null,
    @SerializedName("time") val time: String? = null
)

/** Circuit name and where it is. */
data class CircuitDto(
    @SerializedName("circuitName") val circuitName: String? = null,
    @SerializedName("Location") val location: LocationDto? = null
)

/** City and country of a circuit. */
data class LocationDto(
    @SerializedName("locality") val locality: String? = null,
    @SerializedName("country") val country: String? = null
)