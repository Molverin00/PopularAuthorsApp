package com.example.popularauthorsapp.core.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Performs a network-bound resource retrieval and caching operation.
 *
 * @param ResultType The type of data stored in the local database.
 * @param RequestType The type of data fetched from the remote API.
 * @param query A lambda function that queries the data from the local database as a Flow.
 * @param fetch A suspend function that fetches the data from the remote API.
 * @param saveFetchResult A suspend function that saves the fetched data into the local database.
 * @param shouldFetch A lambda function that determines whether a network request should be made based on the existing data in the local database.
 *
 * @return A Flow of Resource<ResultType> representing the loading state and the data.
 */
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    // Query the data from the local database
    val data = query().first()

    // Check if a network request should be made
    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        try {
            // Fetch data from the remote API and save it to the local database
            saveFetchResult(fetch())

            // Map the locally cached data to Resource.Success and emit
            query().map { Resource.Success(it) }
        } catch (throwable : Throwable) {
            // Map the locally cached data to Resource.Error and emit
            query().map { Resource.Error(throwable, it) }
        }
    } else {
        // Data is up to date, map the locally cached data to Resource.Success and emit
        query().map { Resource.Success(it) }
    }

    // Emit the loading or success/error state
    emitAll(flow)
}