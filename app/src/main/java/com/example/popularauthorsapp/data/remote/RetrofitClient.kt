package com.example.popularauthorsapp.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.popularauthorsapp.BuildConfig

object RetrofitClient {
    private const val BASE_URL = BuildConfig.BASE_URL
    private const val API_KEY = BuildConfig.RAPIDAPI_KEY
    private const val API_HOST = BuildConfig.RAPIDAPI_HOST

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-RapidAPI-Key", API_KEY)
                .addHeader("X-RapidAPI-Host", API_HOST)
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: AuthorsApiService = retrofit.create(AuthorsApiService::class.java)
}