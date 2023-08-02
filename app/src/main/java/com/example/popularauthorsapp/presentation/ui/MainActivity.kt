package com.example.popularauthorsapp.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.popularauthorsapp.data.remote.RetrofitClient.apiService
import com.example.popularauthorsapp.databinding.ActivityMainBinding
import com.example.popularauthorsapp.presentation.adapters.AuthorsAdapter
import com.example.popularauthorsapp.presentation.viewModels.AuthorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val authorViewModel: AuthorViewModel by viewModels()
    private lateinit var authorsAdapter: AuthorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the RecyclerView and its adapter
        authorsAdapter = AuthorsAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = authorsAdapter
        }

        authorViewModel.authors.observe(this@MainActivity) {authors ->
            authorsAdapter.submitList(authors)
        }

        // Observe the LiveData to update the UI when data changes
//        authorViewModel.authors.observe(this@MainActivity, Observer { result ->
//            if (result.data == null) {
//                println("result is empty")
//            } else {
//                authorsAdapter.submitList(result.data)
//            }
//        })
    }
}