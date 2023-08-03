package com.example.popularauthorsapp.data

import com.example.popularauthorsapp.util.networkBoundResource
import com.example.popularauthorsapp.data.local.AppDatabase
import com.example.popularauthorsapp.data.model.Author
import com.example.popularauthorsapp.data.remote.AuthorsApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import java.lang.Exception
import javax.inject.Inject

class AuthorRepository @Inject constructor(
    private val apiService: AuthorsApiService,
    database: AppDatabase
) {

    private val authorDao = database.authorDao()

    /**
     * Retrieves the top authors either from the local database or from the remote API.
     *
     * @return A [Flow] emitting the [Resource] that contains the list of authors.
     * The [Resource] can be in one of three states: [Resource.Loading], [Resource.Success], or [Resource.Error].
     */
    fun getTopAuthors() = networkBoundResource(
        query = {
            // Fetch authors from the local database
            authorDao.getAllAuthors()
        },
        fetch = {
            // Fetch authors from the remote API
            delay(1000) // delay result just to test loading state
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


    /**
     * Forces a fetch of the top authors from the remote API and updates the local database.
     *
     * @return A list of [Author] fetched from the remote API.
     * If an error occurs during the fetch, the list of authors from the local database is returned.
     */
    suspend fun forceFetchTopAuthors(): List<Author> {
        return try {
            val authors = apiService.getTopAuthors()
            authorDao.deleteAllAuthors()
            authorDao.insertAllAuthors(authors)
            authors
        } catch (e: Exception) {
            println(e.localizedMessage)
            authorDao.getAllAuthors().first()
        }
    }


    /**
     * Fetches the top authors manually, either from the local database or from the remote API.
     *
     * @return A list of [Author] fetched from the local database or from the remote API.
     * If the local database is empty, the authors are fetched from the remote API and stored in the local database.
     * If an error occurs during the fetch, an empty list is returned.
     */
    suspend fun fetchTopAuthorsManually(): List<Author> {
            val cachedAuthors = authorDao.getAllAuthors().first()
            return if (cachedAuthors.isNotEmpty()) {
                cachedAuthors
            } else {
                try {
                    val authors = apiService.getTopAuthors()
                    authorDao.deleteAllAuthors()
                    authorDao.insertAllAuthors(authors)
                    authors

                } catch (e : Exception) {
                    println(e.localizedMessage)
                    emptyList()
            }
        }
    }

}