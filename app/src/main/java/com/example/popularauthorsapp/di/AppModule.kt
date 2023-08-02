package com.example.popularauthorsapp.di

import com.example.popularauthorsapp.data.remote.AuthorsApiService
import com.example.popularauthorsapp.data.remote.AuthorsApiService.Companion.API_HOST
import com.example.popularauthorsapp.data.remote.AuthorsApiService.Companion.API_KEY
import com.example.popularauthorsapp.data.remote.AuthorsApiService.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("X-RapidAPI-Key", API_KEY)
                        .addHeader("X-RapidAPI-Host", API_HOST)
                        .build()
                    chain.proceed(request)
                }
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideAuthorApiService(retrofit: Retrofit): AuthorsApiService =
        retrofit.create(AuthorsApiService::class.java)
}