package com.tashev.gbweatherfromya.repository

import com.tashev.gbweatherfromya.dataSource.Weather
import com.tashev.gbweatherfromya.dataSource.getRussianCities
import com.tashev.gbweatherfromya.dataSource.getWorldCities
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.RemoteDataSource
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.WeatherDTO
import retrofit2.Callback


interface DetailsRepository {

    fun getWeatherFromRemoteServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    ): WeatherDTO

    fun getWeatherFromLocalStorageWorld(): List<Weather>
    fun getWeatherFromLocalStorageRus(): List<Weather>
}

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource) : DetailsRepository {

    override fun getWeatherFromRemoteServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    ):WeatherDTO {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
}