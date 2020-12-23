package ru.itrequest.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.delay
import ru.itrequest.network.WaterfallService
import ru.itrequest.util.Result
import java.lang.Exception

/**
 * We don't use cache (local database)
 */
class WaterfallRepository(private val service: WaterfallService) {

    @WorkerThread
    suspend fun getAll(): Result<List<Waterfall>, Throwable> {
        return try {
            // Network latency imitation
            delay(1000)
            Result.Success(service.getAll().payload)
        } catch (e: Exception) {
            Result.Failure(error = e)
        }
    }
}