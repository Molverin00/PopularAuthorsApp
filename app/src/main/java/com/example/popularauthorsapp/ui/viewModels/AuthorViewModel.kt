package com.example.popularauthorsapp.ui.viewModels

import android.app.Application
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

    init {
        val apiService = RetrofitClient.apiService
        val authorDao = AppDatabase.getInstance(application).authorDao()
        repository = AuthorRepository(apiService, authorDao)
        fetchTopAuthors()
    }

    fun fetchTopAuthors() {
        viewModelScope.launch {
            try {
                val authors = repository.getTopAuthors()
                topAuthorsLiveData.postValue(authors)
            } catch (e: Exception) {
                // Handle error here
                Log.e(TAG, "Error fetching authors", e)
                errorLiveData.postValue(true)
            }
        }
    }

    companion object {
        private const val TAG = "AuthorViewModel"
    }
}