package com.example.locationsearch.data.repository

import com.example.locationsearch.data.api.LocationApiService
import com.example.locationsearch.data.model.LocationResult
import com.example.locationsearch.utils.Constants.API_KEY
import javax.inject.Inject

class LocationRepository @Inject constructor(private val apiService: LocationApiService) {
    suspend fun getLocationResult(query: String): List<LocationResult> {
        return apiService.getLocationResult(
            key = API_KEY,
            query = query
        )
    }
}