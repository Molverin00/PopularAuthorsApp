package com.example.popularauthorsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.popularauthorsapp.R
import com.example.popularauthorsapp.ui.adapters.AuthorAdapter
import com.example.popularauthorsapp.ui.viewModels.AuthorViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var authorAdapter: AuthorAdapter
    private lateinit var authorViewModel: AuthorViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        authorAdapter = AuthorAdapter(emptyList()) // Initially empty list
        recyclerView.adapter = authorAdapter

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            fetchTopAuthors()
        }

        authorViewModel = ViewModelProvider(this).get(AuthorViewModel::class.java)
        authorViewModel.topAuthorsLiveData.observe(this, Observer { authors ->
            authorAdapter.setData(authors)
            swipeRefreshLayout.isRefreshing = false // Hide the refresh indicator
        })

        authorViewModel.errorLiveData.observe(this, Observer { error ->
            if (error) {
                showErrorSnackbar()
                swipeRefreshLayout.isRefreshing = false // Hide the refresh indicator
            }
        })

        fetchTopAuthors()
    }

    private fun fetchTopAuthors() {
        swipeRefreshLayout.isRefreshing = true // Show the refresh indicator
        authorViewModel.fetchTopAuthors()
    }

    private fun showErrorSnackbar() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Error fetching authors.",
            Snackbar.LENGTH_LONG
        ).show()
    }
}