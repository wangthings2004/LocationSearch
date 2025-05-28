package com.example.locationsearch.data.api

import com.example.locationsearch.data.model.LocationResult
import com.example.locationsearch.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApiService {
    @GET("v1/autocomplete")
    suspend fun getLocationResult(
        @Query("key")  key :String = API_KEY,
        @Query("q") query : String,
        @Query("limit") limit: Int =  10,
        @Query("dedupe") depupe : Int = 1
    ): List<LocationResult>
}