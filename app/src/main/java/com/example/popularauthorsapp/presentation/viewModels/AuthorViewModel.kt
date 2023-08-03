package com.example.popularauthorsapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.example.popularauthorsapp.data.AuthorRepository
import com.example.popularauthorsapp.data.model.Author
import com.example.popularauthorsapp.util.ConnectivityMonitor
import com.example.popularauthorsapp.util.NetworkConnectivityMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AuthorViewModel @Inject constructor(
    private val repository: AuthorRepository,
    networkConnectivityMonitor: NetworkConnectivityMonitor
) : ViewModel() {

    /**
     * A flow representing the network connectivity status observed using [networkConnectivityMonitor].
     * It emits [ConnectivityMonitor.Status] values indicating the availability of the internet connection.
     */
    val networkStatus: Flow<ConnectivityMonitor.Status> = networkConnectivityMonitor.observe()


    /**
     * A flow representing the top authors fetched from the [repository].
     * It emits [Resource] with [List] of [Author] data, indicating the status of the fetch operation.
     * Use this flow to observe and receive updated author data in real-time.
     */
    val authors = repository.getTopAuthors()


    /**
     * A suspend function used to force-fetch the top authors from the [repository].
     * This function initiates a network request regardless of the current cached data and fetches the latest authors.
     * Use this function when you want to ensure that the latest data is fetched from the remote API.
     *
     * @return A [List] of [Author] representing the latest authors fetched from the API.
     */
    suspend fun forceFetchTopAuthors(): List<Author> {
        return repository.forceFetchTopAuthors()
    }


    /**
     * A suspend function used to manually fetch the top authors from the [repository].
     * This function first tries to fetch authors from the local database (cached data),
     * and if it is empty, it initiates a network request to fetch authors from the remote API.
     * Use this function to avoid unnecessary network requests if the data is already available in the local database.
     *
     * @return A [List] of [Author] representing the fetched authors from either the local database or the remote API.
     */
    suspend fun fetchTopAuthorsManually(): List<Author> {
        return repository.fetchTopAuthorsManually()
    }
}