package com.example.popularauthorsapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.popularauthorsapp.data.local.AppDatabase
import com.example.popularauthorsapp.data.remote.AuthorsApiService
import com.example.popularauthorsapp.data.remote.AuthorsApiService.Companion.API_HOST
import com.example.popularauthorsapp.data.remote.AuthorsApiService.Companion.API_KEY
import com.example.popularauthorsapp.data.remote.AuthorsApiService.Companion.BASE_URL
import com.example.popularauthorsapp.data.local.AppDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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


    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, DATABASE_NAME)
            .build()


    @Provides
    fun provideCircularProgressDrawable(@ApplicationContext context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
    }
}