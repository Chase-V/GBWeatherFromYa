package com.tashev.weatherie.repository.weatherLoaderAndDTO

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForecastDTO(
    val parts: List<Part>
) : Parcelable

@Parcelize
data class Part(
    val part_name: String,
    val temp_min: Int,
    val temp_avg: Int,
    val temp_max: Int,
    val wind_speed: Double,
    val humidity: Long,
    val icon: String,
    val condition: String,
    val feels_like: Long,
) : Parcelable