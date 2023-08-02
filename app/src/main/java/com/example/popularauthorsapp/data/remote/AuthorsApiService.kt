package com.example.popularauthorsapp.data.remote

import androidx.lifecycle.LiveData
import com.example.popularauthorsapp.BuildConfig
import com.example.popularauthorsapp.data.model.Author
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface AuthorsApiService {

    companion object {
        const val BASE_URL = BuildConfig.BASE_URL
        const val API_KEY = BuildConfig.RAPIDAPI_KEY
        const val API_HOST = BuildConfig.RAPIDAPI_HOST
    }

    @GET("top_authors")
    suspend fun getTopAuthors(): List<Author>
}