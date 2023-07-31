package com.example.popularauthorsapp.data

import com.example.popularauthorsapp.data.local.AuthorDao
import com.example.popularauthorsapp.data.model.Author
import com.example.popularauthorsapp.data.remote.AuthorsApiService

class AuthorRepository(private val apiService: AuthorsApiService, private val authorDao: AuthorDao) {
    suspend fun getTopAuthors(): List<Author> {
        val cachedAuthors = authorDao.getAllAuthors()
        return if (cachedAuthors.isNotEmpty()) {
            cachedAuthors
        } else {
            val authors = apiService.getTopAuthors()
            authorDao.insertAll(authors)
            authors
        }
    }
}