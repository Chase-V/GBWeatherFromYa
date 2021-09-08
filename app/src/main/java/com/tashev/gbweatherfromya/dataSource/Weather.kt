package com.tashev.gbweatherfromya.dataSource

data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 25,
    val feelsLike: Int = 18
)

fun getDefaultCity() = City("Москва", 55.755826, 37.617299900000035)