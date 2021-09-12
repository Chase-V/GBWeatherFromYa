package com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO

data class ForecastDTO(
    val parts: List<Part>
)

data class Part (
    val part_name: String,
    val temp_min: Int,
    val temp_avg: Int,
    val temp_max: Int,
    val wind_speed: Double,
    val humidity: Long,
    val icon: String,
    val condition: String,
    val feels_like: Long,
)