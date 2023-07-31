package com.example.popularauthorsapp.data.remote

import androidx.lifecycle.LiveData
import com.example.popularauthorsapp.data.model.Author
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface AuthorsApiService {
    @GET("top_authors")
    suspend fun getTopAuthors(): List<Author>
}