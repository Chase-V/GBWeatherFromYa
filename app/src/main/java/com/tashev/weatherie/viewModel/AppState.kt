package com.tashev.weatherie.viewModel

import com.tashev.weatherie.dataSource.Weather

sealed class AppState {
    data class Success(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
