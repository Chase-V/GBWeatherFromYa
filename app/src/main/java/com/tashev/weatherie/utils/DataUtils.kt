package com.tashev.weatherie.utils

import com.tashev.weatherie.dataSource.*
import com.tashev.weatherie.repository.room.HistoryEntity
import com.tashev.weatherie.repository.weatherLoaderAndDTO.WeatherDTO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun convertDTOtoModel(weatherDTO: WeatherDTO): List<Weather> {

    val listParts = mutableListOf<Parts>()

    weatherDTO.forecast.parts.forEach {
        listParts.add(
            Parts(
                it.part_name,
                it.temp_min,
                it.temp_avg,
                it.temp_max,
                it.wind_speed,
                it.humidity,
                it.icon,
                it.condition,
                it.feels_like
            )
        )
    }

    return listOf(
        Weather(
            WeatherFact(
                getDefaultCity(),
                weatherDTO.fact.temp,
                weatherDTO.fact.feels_like,
                weatherDTO.fact.wind_speed,
                weatherDTO.fact.humidity,
                weatherDTO.fact.condition,
                weatherDTO.fact.pressure_mm,
                weatherDTO.fact.icon
            ),
            WeatherForecast(
                listParts
            )
        )
    )
}

fun convertHistoryEntityToWeather(entityList:List<HistoryEntity>):List<Weather>{
    return entityList.map{
        Weather(
            WeatherFact(
                City(it.name),
                it.temp,
                it.feelsLike,
                it.windSpeed,
                it.humidity,
                it.condition,
                it.pressure,
            )
        )
    }
}

fun convertWeatherToHistoryEntity(weather: Weather):HistoryEntity{
    return HistoryEntity(0,
        weather.weatherFact.city.name,
        weather.weatherFact.temperature,
        weather.weatherFact.feelsLike,
        weather.weatherFact.windSpeed,
        weather.weatherFact.humidity,
        weather.weatherFact.condition,
        weather.weatherFact.pressure,
    )
}