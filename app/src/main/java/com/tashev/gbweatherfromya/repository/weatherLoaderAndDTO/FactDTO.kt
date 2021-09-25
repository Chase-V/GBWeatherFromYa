package com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FactDTO(
    val temp: Int,
    val feels_like: Int,
    val condition: String,
    val wind_speed: Double,
    val humidity: Int,
    val icon: String,
    val pressure_mm: Int
):Parcelable