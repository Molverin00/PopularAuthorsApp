package com.example.popularauthorsapp.util

import android.content.Context
import android.net.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * NetworkConnectivityMonitor is an implementation of the [ConnectivityMonitor] interface that
 * observes the network connectivity status of the device.
 *
 * @param context The application context used to access the ConnectivityManager.
 */
class NetworkConnectivityMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) : ConnectivityMonitor {

    // ConnectivityManager to manage network connectivity callbacks
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Observes the network connectivity status of the device.
     *
     * @return A [Flow] that emits [ConnectivityMonitor.Status] indicating the current network
     * connectivity status.
     */
    override fun observe(): Flow<ConnectivityMonitor.Status> {
        return callbackFlow {

            // Get the initial connectivity status and send it immediately to the flow
            val initialStatus = getInitialConnectivityStatus()
            launch { send(initialStatus) }

            // Create a network callback to receive network status updates
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    // Send the network status to the flow when it is available
                    launch { send(ConnectivityMonitor.Status.Available) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    // Send the network status to the flow when it is lost
                    launch { send(ConnectivityMonitor.Status.Lost) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    // Send the network status to the flow when it is unavailable
                    launch { send(ConnectivityMonitor.Status.Unavailable) }
                }
            }

            // Register the network callback to receive network status updates
            connectivityManager.registerDefaultNetworkCallback(callback)

            // Close the flow and unregister the network callback when it is no longer active
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()

    }

    // Helper function to get the initial connectivity status
    private fun getInitialConnectivityStatus(): ConnectivityMonitor.Status {
        return if (isConnected()) {
            ConnectivityMonitor.Status.Available
        } else {
            ConnectivityMonitor.Status.Unavailable
        }
    }

    // Helper function to check if there is an active internet connection
    private fun isConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}