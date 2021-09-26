package com.tashev.gbweatherfromya.utils

import com.tashev.gbweatherfromya.dataSource.*
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.WeatherDTO

fun convertDTO(weatherDTO: WeatherDTO): List<Weather> {
    return listOf(
        Weather(
            WeatherFact(
                getDefaultCity(),
                weatherDTO.fact.temp,
                weatherDTO.fact.feels_like,
                weatherDTO.fact.wind_speed,
                weatherDTO.fact.humidity,
                weatherDTO.fact.condition
            ),
            WeatherForecast(
                Parts(
                    weatherDTO.forecast.parts.
                )
            )

        )
}

//data class Weather(
//    private val weatherFact: WeatherFact,
//    private val weatherForecast: WeatherForecast
//) : Parcelable
//
//data class WeatherFact(
//    val city: City = getDefaultCity(),
//    val temperature: Int = 25,
//    val feelsLike: Int = 18,
//    val windSpeed: Double = 5.0,
//    val humidity: Int = 54,
//    val condition: String = "Ясно"
//)
//
//
//data class WeatherForecast(
//    val parts: List<Parts>
//)
//
//data class Parts(
//    val partName: String = "day",
//    val tempMin: Int = 20,
//    val tempAvg: Int = 25,
//    val tempMax: Int = 30,
//    val windSpeed: Double = 3.0,
//    val humidity: Long = 55,
//    val icon: String = "skc_n",
//    val condition: String = "Ясно",
//    val feelsLike: Long = 22,
//)