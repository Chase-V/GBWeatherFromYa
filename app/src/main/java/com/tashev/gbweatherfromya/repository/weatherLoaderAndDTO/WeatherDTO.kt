package com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDTO(
    val fact: FactDTO,
    val forecast: ForecastDTO
) : Parcelable
