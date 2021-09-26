package com.tashev.gbweatherfromya.viewModel

import com.tashev.gbweatherfromya.dataSource.Weather
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.WeatherDTO

sealed class AppState {
    data class Success(val weatherData: WeatherDTO) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
