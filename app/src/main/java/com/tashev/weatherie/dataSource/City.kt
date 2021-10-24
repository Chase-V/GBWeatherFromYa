package com.tashev.weatherie.dataSource

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val name: String = "Севастополь",
    val lat: Double = 0.0,
    val lon: Double = 0.0
) : Parcelable