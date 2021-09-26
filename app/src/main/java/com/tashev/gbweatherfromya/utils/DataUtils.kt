package com.tashev.gbweatherfromya.utils

import com.tashev.gbweatherfromya.dataSource.*
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.WeatherDTO

fun convertDTO(weatherDTO: WeatherDTO): List<Weather> {

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