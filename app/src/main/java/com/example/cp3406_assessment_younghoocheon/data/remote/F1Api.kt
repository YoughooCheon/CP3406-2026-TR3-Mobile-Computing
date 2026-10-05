package com.example.cp3406_assessment_younghoocheon.data.remote

import retrofit2.http.GET

/** Retrofit definition of the Jolpica F1 web API (successor of the Ergast API). */
interface F1Api {

    /** Returns the next race of the current season (empty race list once the season is over). */
    @GET("ergast/f1/current/next.json")
    suspend fun getNextRace(): NextRaceResponse

    companion object {
        /** Retrofit requires the base URL to end with a slash. */
        const val BASE_URL = "https://api.jolpi.ca/"
    }
}
