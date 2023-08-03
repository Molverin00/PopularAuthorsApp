package com.example.popularauthorsapp.util

import kotlinx.coroutines.flow.Flow

/**
 * The `ConnectivityMonitor` interface defines a contract for classes that monitor the network
 * connectivity status of the device.
 */
interface ConnectivityMonitor {

    /**
     * The `Status` enum class represents the possible network connectivity statuses.
     */
    enum class Status {
        Available, Unavailable, Lost
    }


    /**
     * Observes the network connectivity status of the device.
     *
     * @return A [Flow] that emits [Status] indicating the current network connectivity status.
     */
    fun observe(): Flow<Status>
}