package com.example.cp3406_assessment_younghoocheon.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/** Retrofit definition of the official formula1.com website (news feed and article pages). */
interface F1NewsApi {

    /** Returns the raw RSS (XML) text; [parseNewsFeed] turns it into articles. */
    @GET("en/latest/all.xml")
    suspend fun getLatestNews(): ResponseBody

    /**
     * Opens one article page. It is streamed so the caller can read just the top of the page
     * (where the photo address is) and close the connection without downloading the rest.
     */
    @Streaming
    @GET
    suspend fun getArticlePage(@Url url: String): ResponseBody

    companion object {
        /** Retrofit requires the base URL to end with a slash. */
        const val BASE_URL = "https://www.formula1.com/"
    }
}