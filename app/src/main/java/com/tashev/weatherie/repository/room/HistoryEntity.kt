package com.tashev.weatherie.repository.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val temp: Int,
    val feelsLike: Int,
    val windSpeed: Double,
    val humidity: Int,
    val condition: String,
    val pressure: Int
)