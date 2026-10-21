package com.example.cp3406_assessment_younghoocheon.data

import com.example.cp3406_assessment_younghoocheon.data.remote.F1Api
import com.example.cp3406_assessment_younghoocheon.data.remote.MrDataDto
import com.example.cp3406_assessment_younghoocheon.data.remote.NextRaceResponse
import com.example.cp3406_assessment_younghoocheon.data.remote.RaceDto
import com.example.cp3406_assessment_younghoocheon.data.remote.RaceTableDto
import com.example.cp3406_assessment_younghoocheon.data.repository.DefaultRaceRepository
import com.example.cp3406_assessment_younghoocheon.data.repository.RaceError
import com.example.cp3406_assessment_younghoocheon.data.repository.RaceLoadException
import java.io.IOException
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class DefaultRaceRepositoryTest {

    /** Fake Retrofit API whose behaviour is supplied by each test. */
    private class FakeApi(private val response: () -> NextRaceResponse) : F1Api {
        override suspend fun getNextRace(): NextRaceResponse = response()
    }

    private fun responseWith(vararg races: RaceDto) =
        NextRaceResponse(MrDataDto(RaceTableDto(races.toList())))

    private fun errorOf(result: Result<*>): RaceError {
        val failure = result.exceptionOrNull()
        assertTrue("Expected RaceLoadException but was $failure", failure is RaceLoadException)
        return (failure as RaceLoadException).error
    }

    @Test
    fun success_returnsMappedRace(): Unit = runBlocking {
        val repository = DefaultRaceRepository(
            FakeApi {
                responseWith(
                    RaceDto(
                        round = "7",
                        raceName = "Monaco Grand Prix",
                        date = "2025-05-25",
                        time = "13:00:00Z"
                    )
                )
            }
        )

        val result = repository.getNextRace()

        assertTrue(result.isSuccess)
        assertEquals("Monaco Grand Prix", result.getOrNull()?.name)
        assertEquals(7, result.getOrNull()?.round)
    }

    @Test
    fun emptyRaceList_isReportedAsNoRace(): Unit = runBlocking {
        val repository = DefaultRaceRepository(FakeApi { responseWith() })

        assertEquals(RaceError.NO_RACE, errorOf(repository.getNextRace()))
    }

    @Test
    fun missingBody_isReportedAsNoRace(): Unit = runBlocking {
        val repository = DefaultRaceRepository(FakeApi { NextRaceResponse() })

        assertEquals(RaceError.NO_RACE, errorOf(repository.getNextRace()))
    }

    @Test
    fun ioException_isReportedAsNetworkError(): Unit = runBlocking {
        val repository = DefaultRaceRepository(FakeApi { throw IOException("offline") })

        assertEquals(RaceError.NETWORK, errorOf(repository.getNextRace()))
    }

    @Test
    fun httpException_isReportedAsServerError(): Unit = runBlocking {
        val errorResponse = Response.error<NextRaceResponse>(500, "".toResponseBody())
        val repository = DefaultRaceRepository(FakeApi { throw HttpException(errorResponse) })

        assertEquals(RaceError.SERVER, errorOf(repository.getNextRace()))
    }

    @Test
    fun unusableRace_isReportedAsDataError(): Unit = runBlocking {
        val repository = DefaultRaceRepository(FakeApi { responseWith(RaceDto(raceName = "No date")) })

        assertEquals(RaceError.DATA, errorOf(repository.getNextRace()))
    }
}