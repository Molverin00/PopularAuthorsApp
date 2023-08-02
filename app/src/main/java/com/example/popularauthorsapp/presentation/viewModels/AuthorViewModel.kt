package com.example.popularauthorsapp.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.popularauthorsapp.data.model.Author
import com.example.popularauthorsapp.data.remote.AuthorsApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorViewModel @Inject constructor(apiService: AuthorsApiService) : ViewModel() {

    private val authorsLiveData = MutableLiveData<List<Author>>()
    val authors: LiveData<List<Author>> = authorsLiveData

    init {
        viewModelScope.launch {
            val authors = apiService.getTopAuthors()
            delay(2000)
            authorsLiveData.value = authors
        }
    }

//    val authors = repository.getTopAuthors().asLiveData()

//    private val repository: AuthorRepository
//    val topAuthorsLiveData: MutableLiveData<List<Author>> = MutableLiveData()
//    val errorLiveData: MutableLiveData<Boolean> = MutableLiveData()
//    val networkStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
//    val connectedLiveData: MutableLiveData<Boolean> = MutableLiveData()
//
//    private var isConnected = false // To track network status
//
//    init {
//        val apiService = RetrofitClient.apiService
//        val authorDao = AppDatabase.getInstance(application).authorDao()
//        repository = AuthorRepository(apiService, authorDao)
//        fetchTopAuthors()
//
//        // Observe network changes
//        val connectivityManager =
//            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//                isConnected = true
//                networkStatusLiveData.postValue(true)
//            }
//
//            override fun onLost(network: Network) {
//                isConnected = false
//                networkStatusLiveData.postValue(false)
//            }
//        })
//    }
//
//    fun fetchTopAuthors() {
//        viewModelScope.launch {
//            try {
//                val authors = repository.getTopAuthors()
//                authors.collect()
////                topAuthorsLiveData.postValue(authors)
//                if (!isConnected) {
//                    connectedLiveData.postValue(true) // Notify the MainActivity about the successful connection
//                }
//            } catch (e: Exception) {
//                // Handle error here
//                errorLiveData.postValue(true)
//            }
//        }
//    }
}