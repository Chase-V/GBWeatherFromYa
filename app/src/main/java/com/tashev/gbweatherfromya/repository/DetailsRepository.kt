package com.tashev.gbweatherfromya.repository

import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.RemoteDataSource
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.WeatherDTO
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