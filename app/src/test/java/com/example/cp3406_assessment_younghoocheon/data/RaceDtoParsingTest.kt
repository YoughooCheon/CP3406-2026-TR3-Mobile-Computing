package com.example.cp3406_assessment_younghoocheon.data

import com.example.cp3406_assessment_younghoocheon.data.mapper.toDomain
import com.example.cp3406_assessment_younghoocheon.data.model.SessionType
import com.example.cp3406_assessment_younghoocheon.data.remote.NextRaceResponse
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/** Makes sure the Gson annotations match the real Jolpica JSON field names. */
class RaceDtoParsingTest {

    private val sampleJson = """
        {
          "MRData": {
            "series": "f1",
            "RaceTable": {
              "season": "2024",
              "Races": [
                {
                  "season": "2024",
                  "round": "23",
                  "raceName": "Qatar Grand Prix",
                  "Circuit": {
                    "circuitId": "losail",
                    "circuitName": "Losail International Circuit",
                    "Location": { "lat": "25.49", "long": "51.4542", "locality": "Al Daayen", "country": "Qatar" }
                  },
                  "date": "2024-12-01",
                  "time": "16:00:00Z",
                  "FirstPractice": { "date": "2024-11-29", "time": "13:30:00Z" },
                  "Qualifying": { "date": "2024-11-30", "time": "18:00:00Z" },
                  "Sprint": { "date": "2024-11-30", "time": "14:00:00Z" },
                  "SprintQualifying": { "date": "2024-11-29", "time": "17:30:00Z" }
                }
              ]
            }
          }
        }
    """.trimIndent()

    @Test
    fun realWorldJson_isParsedAndMapped() {
        val response = Gson().fromJson(sampleJson, NextRaceResponse::class.java)
        val dto = response.mrData?.raceTable?.races?.firstOrNull()
        assertNotNull(dto)

        val race = dto!!.toDomain()

        assertEquals("Qatar Grand Prix", race.name)
        assertEquals("Losail International Circuit", race.circuitName)
        assertEquals("Al Daayen", race.locality)
        assertEquals(23, race.round)
        assertEquals(
            listOf(
                SessionType.FIRST_PRACTICE,
                SessionType.SPRINT_QUALIFYING,
                SessionType.SPRINT,
                SessionType.QUALIFYING,
                SessionType.RACE
            ),
            race.sessions.map { it.type }
        )
    }
}
