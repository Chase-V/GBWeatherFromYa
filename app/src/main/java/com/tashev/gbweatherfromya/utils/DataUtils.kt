package com.tashev.gbweatherfromya.utils

import com.tashev.gbweatherfromya.dataSource.Weather
import com.tashev.gbweatherfromya.dataSource.getDefaultCity
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.WeatherDTO

fun convertDTO(weatherDTO: WeatherDTO): List<Weather> {
    return listOf(
        Weather(
            getDefaultCity(),
            weatherDTO.fact.temp,
            weatherDTO.fact.feels_like,
            weatherDTO.fact.wind_speed,
            weatherDTO.fact.humidity,
            weatherDTO.fact.condition,
        )
    )
}