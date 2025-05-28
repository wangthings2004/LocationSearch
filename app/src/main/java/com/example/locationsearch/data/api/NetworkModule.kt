package com.example.locationsearch.data.api

import com.example.locationsearch.data.repository.LocationRepository
import com.example.locationsearch.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideLocationApiService(retrofit: Retrofit) : LocationApiService {
        return retrofit.create(LocationApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(apiService: LocationApiService): LocationRepository {
        return LocationRepository(apiService)
    }
}