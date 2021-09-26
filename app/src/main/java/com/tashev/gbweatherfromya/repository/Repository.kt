package com.tashev.gbweatherfromya.repository

import com.tashev.gbweatherfromya.dataSource.Weather
import com.tashev.gbweatherfromya.dataSource.getRussianCities
import com.tashev.gbweatherfromya.dataSource.getWorldCities

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorageWorld(): List<Weather>
    fun getWeatherFromLocalStorageRus(): List<Weather>
}

class RepositoryImpl : Repository {

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getRussianCities()
    }
}