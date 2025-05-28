package com.example.locationsearch.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationsearch.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchResultAdapter
    private var searchJob: Job? = null
    private var currentQuery: String = ""
    private val activityScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupAdapter()
        setupListener()
        setupObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }

    private fun setupUI() {
        binding.ivX.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.ivSearch.visibility = View.VISIBLE
    }

    private fun setupAdapter() {
        adapter = SearchResultAdapter()
        binding.rvSearchHint.layoutManager = LinearLayoutManager(this)
        binding.rvSearchHint.adapter = adapter

        adapter.onItemClick = { result ->
            val gmmIntentUri = "geo:${result.lat},${result.lon}?q=${result.displayName}".toUri()
            val ggMapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            ggMapIntent.setPackage("com.google.android.apps.maps")
            startActivity(ggMapIntent)
        }
    }

    private fun setupListener() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(text: Editable?) {
                val query = text.toString().trim()
                searchJob?.cancel()
                binding.ivSearch.visibility = View.GONE
                searchJob = launch {
                    delay(100)
                    if (query != currentQuery) {
                        currentQuery = query
                        binding.ivX.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE
                        viewModel.locationSearch(query)
                    }
                }
            }
        })

        binding.ivX.setOnClickListener {
            binding.rvSearchHint.visibility = View.GONE
            binding.edtSearch.text.clear()
            binding.ivX.visibility = View.GONE
            binding.ivSearch.visibility = View.VISIBLE
        }
    }

    private fun setupObserver() {
        viewModel.searchResults.observe(this) { results ->
            adapter.submitLimitedList(results, currentQuery)
            binding.rvSearchHint.visibility = if (results.isNotEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if(isLoading) {
                binding.ivSearch.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }else {
                binding.ivSearch.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return activityScope.launch(block = block)
    }
}