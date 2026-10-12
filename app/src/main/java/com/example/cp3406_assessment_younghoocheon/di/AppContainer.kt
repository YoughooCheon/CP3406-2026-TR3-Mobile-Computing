package com.example.cp3406_assessment_younghoocheon.di

import com.example.cp3406_assessment_younghoocheon.data.remote.F1Api
import com.example.cp3406_assessment_younghoocheon.data.remote.F1NewsApi
import com.example.cp3406_assessment_younghoocheon.data.repository.DefaultNewsRepository
import com.example.cp3406_assessment_younghoocheon.data.repository.DefaultRaceRepository
import com.example.cp3406_assessment_younghoocheon.data.repository.NewsRepository
import com.example.cp3406_assessment_younghoocheon.data.repository.RaceRepository
import java.util.concurrent.TimeUnit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val NETWORK_TIMEOUT_SECONDS = 15L
private const val USER_AGENT = "CP3406-F1-RaceCountdown/1.0 (student project)"

/** Manual dependency-injection container: the one place where objects are wired together. */
interface AppContainer {
    val raceRepository: RaceRepository
    val newsRepository: NewsRepository
}

/** Production container. Objects are created lazily and shared for the app's lifetime. */
class DefaultAppContainer : AppContainer {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(
                Interceptor { chain ->
                    // Identify the client politely to the free public API.
                    chain.proceed(
                        chain.request().newBuilder()
                            .header("User-Agent", USER_AGENT)
                            .build()
                    )
                }
            )
            .build()
    }

    private val api: F1Api by lazy {
        Retrofit.Builder()
            .baseUrl(F1Api.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(F1Api::class.java)
    }

    // Second Retrofit instance: the news RSS feed lives on formula1.com, not on the Jolpica server.
    // No converter factory is needed because the raw XML is parsed by parseNewsFeed().
    private val newsApi: F1NewsApi by lazy {
        Retrofit.Builder()
            .baseUrl(F1NewsApi.BASE_URL)
            .client(okHttpClient)
            .build()
            .create(F1NewsApi::class.java)
    }

    override val raceRepository: RaceRepository by lazy { DefaultRaceRepository(api) }

    override val newsRepository: NewsRepository by lazy { DefaultNewsRepository(newsApi) }
}