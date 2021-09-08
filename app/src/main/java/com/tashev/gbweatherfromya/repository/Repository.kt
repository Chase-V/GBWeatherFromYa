package com.tashev.gbweatherfromya.repository

import com.tashev.gbweatherfromya.dataSource.Weather

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather
}