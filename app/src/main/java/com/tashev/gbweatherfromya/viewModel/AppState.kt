package com.tashev.gbweatherfromya.viewModel

import com.tashev.gbweatherfromya.dataSource.Weather

sealed class AppState {
    data class Success(val weatherData: Weather) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
