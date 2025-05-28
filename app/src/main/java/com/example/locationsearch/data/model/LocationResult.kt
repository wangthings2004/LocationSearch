package com.example.locationsearch.data.model

import com.google.gson.annotations.SerializedName

data class LocationResult(
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("display_name")
    val displayName: String,
    val lat: String,
    val lon: String
)