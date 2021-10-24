package com.tashev.weatherie.repository

import com.tashev.weatherie.repository.weatherLoaderAndDTO.RemoteDataSource
import com.tashev.weatherie.repository.weatherLoaderAndDTO.WeatherDTO
import retrofit2.Callback


interface DetailsRepository {

    fun getWeatherFromRemoteServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    )
}

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource) : DetailsRepository {

    override fun getWeatherFromRemoteServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }
}