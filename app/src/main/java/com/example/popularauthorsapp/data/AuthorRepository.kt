package com.example.popularauthorsapp.data

import com.example.popularauthorsapp.core.util.networkBoundResource
import com.example.popularauthorsapp.data.local.AppDatabase
import com.example.popularauthorsapp.data.remote.AuthorsApiService
import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthorRepository @Inject constructor(
    private val apiService: AuthorsApiService,
    private val database: AppDatabase
) {

    private val authorDao = database.authorDao()

    fun getTopAuthors() = networkBoundResource(
        query = {
            // Fetch authors from the local database
            authorDao.getAllAuthors()
        },
        fetch = {
            // Fetch authors from the remote API
            delay(2000) // delay result just to test loading state
            apiService.getTopAuthors()
        },
        saveFetchResult = { authors ->
            // Replace old data with the fetched authors in the local database
            authorDao.deleteAllAuthors()
            authorDao.insertAllAuthors(authors)
        },
        shouldFetch = { data ->
            // Perform a network request only if the local database is empty
            data.isEmpty() // TODO: Change this condition to change caching strategy
        }
    )
}