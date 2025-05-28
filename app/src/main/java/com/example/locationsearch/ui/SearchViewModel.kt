package com.example.locationsearch.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locationsearch.data.model.LocationResult
import com.example.locationsearch.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {
    private val _searchResults = MutableLiveData<List<LocationResult>>()
    val searchResults: LiveData<List<LocationResult>> get() = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun locationSearch(query: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                if (query.isNotEmpty()) {
                    val results = repository.getLocationResult(query)
                    _searchResults.postValue(results)
                } else {
                    _searchResults.postValue(emptyList())
                }
            } catch (e: Exception) {
                _searchResults.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}