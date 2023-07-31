package com.example.popularauthorsapp.ui.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.popularauthorsapp.data.AuthorRepository
import com.example.popularauthorsapp.data.local.AppDatabase
import com.example.popularauthorsapp.data.model.Author
import com.example.popularauthorsapp.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class AuthorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuthorRepository
    val topAuthorsLiveData: MutableLiveData<List<Author>> = MutableLiveData()
    val errorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val networkStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val connectedLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private var isConnected = false // To track network status

    init {
        val apiService = RetrofitClient.apiService
        val authorDao = AppDatabase.getInstance(application).authorDao()
        repository = AuthorRepository(apiService, authorDao)
        fetchTopAuthors()

        // Observe network changes
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected = true
                networkStatusLiveData.postValue(true)
            }

            override fun onLost(network: Network) {
                isConnected = false
                networkStatusLiveData.postValue(false)
            }
        })
    }

    fun fetchTopAuthors() {
        viewModelScope.launch {
            try {
                val authors = repository.getTopAuthors()
                topAuthorsLiveData.postValue(authors)
                if (!isConnected) {
                    connectedLiveData.postValue(true) // Notify the MainActivity about the successful connection
                }
            } catch (e: Exception) {
                // Handle error here
                errorLiveData.postValue(true)
            }
        }
    }
}