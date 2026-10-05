package com.example.cp3406_assessment_younghoocheon.data.repository

import com.example.cp3406_assessment_younghoocheon.data.mapper.toDomain
import com.example.cp3406_assessment_younghoocheon.data.model.Race
import com.example.cp3406_assessment_younghoocheon.data.remote.F1Api
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

/** [RaceRepository] backed by the Jolpica F1 web API. */
class DefaultRaceRepository(
    private val api: F1Api
) : RaceRepository {

    override suspend fun getNextRace(): Result<Race> = try {
        val raceDto = api.getNextRace()
            .mrData
            ?.raceTable
            ?.races
            ?.firstOrNull()
            ?: throw RaceLoadException(RaceError.NO_RACE)
        Result.success(raceDto.toDomain())
    } catch (e: CancellationException) {
        // Never swallow cancellation, otherwise structured concurrency breaks.
        throw e
    } catch (e: RaceLoadException) {
        Result.failure(e)
    } catch (e: IOException) {
        Result.failure(RaceLoadException(RaceError.NETWORK, e))
    } catch (e: HttpException) {
        Result.failure(RaceLoadException(RaceError.SERVER, e))
    } catch (e: Exception) {
        // JSON parsing problems or an unusable race entry.
        Result.failure(RaceLoadException(RaceError.DATA, e))
    }
}
