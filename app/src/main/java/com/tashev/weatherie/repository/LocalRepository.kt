package com.tashev.weatherie.repository

import com.tashev.weatherie.dataSource.Weather
import com.tashev.weatherie.repository.room.HistoryDAO
import com.tashev.weatherie.utils.convertHistoryEntityToWeather
import com.tashev.weatherie.utils.convertWeatherToHistoryEntity

interface LocalRepository {
    fun getAllHistory():List<Weather>
    fun saveEntity(weather: Weather)
}

class LocalRepositoryImpl(private val localDataSource:HistoryDAO):LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToHistoryEntity(weather))
    }
}