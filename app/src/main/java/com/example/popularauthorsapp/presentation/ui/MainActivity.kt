package com.example.popularauthorsapp.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.popularauthorsapp.R
import com.example.popularauthorsapp.util.Resource
import com.example.popularauthorsapp.databinding.ActivityMainBinding
import com.example.popularauthorsapp.presentation.adapters.AuthorsAdapter
import com.example.popularauthorsapp.presentation.viewModels.AuthorViewModel
import com.example.popularauthorsapp.util.ConnectivityMonitor
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val authorViewModel: AuthorViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var authorsAdapter: AuthorsAdapter
    private lateinit var snackBar: Snackbar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Init views
        initAuthorsRecyclerview()
        initNetworkStatusSnackBar()
        initSwipeRefreshLayout()


        // Observe network status flow
        observeNetworkStatus()

        // Observe authors flow
        observeTopAuthors()
    }


    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            forceFetchTopAuthors()
        }
    }


    private fun observeTopAuthors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authorViewModel.authors.collect { result ->

                    if (result.data != null) {
                        println("result.data.size: ${result.data.size}")
                        authorsAdapter.submitList(result.data)
                    } else {
                        println("result.data is null")
                    }

                    binding.ivEmptyAuthorList.isVisible =
                        result is Resource.Error && result.data.isNullOrEmpty()
                    binding.pbLoadingProgressBar.isVisible =
                        result is Resource.Loading && result.data.isNullOrEmpty()
                    binding.tvErrorMessage.isVisible =
                        result is Resource.Error && result.data.isNullOrEmpty()
                    binding.tvErrorMessage.text = result.error?.localizedMessage

                    swipeRefreshLayout.isRefreshing = false
                }

            }
        }
    }


    private fun observeNetworkStatus() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authorViewModel.networkStatus.collect { status ->
                    println("isConnected: ${status == ConnectivityMonitor.Status.Available}")
                    showNetworkStatusSnackBar(status == ConnectivityMonitor.Status.Available)

                    if (status == ConnectivityMonitor.Status.Available) {
                        authorViewModel.fetchTopAuthorsManually()
                    }
                }
            }
        }
    }


    private fun forceFetchTopAuthors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authorViewModel.forceFetchTopAuthors()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }


    private fun initAuthorsRecyclerview() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = authorsAdapter
        }
    }


    private fun initNetworkStatusSnackBar() {
        snackBar = Snackbar.make(
            binding.root,
            getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.btn_label_dismiss) {
            snackBar.dismiss()
        }
    }


    private fun showNetworkStatusSnackBar(isConnected: Boolean) {
        if (!isConnected) {
            snackBar.show()
        } else {
            snackBar.dismiss()
        }
    }
}