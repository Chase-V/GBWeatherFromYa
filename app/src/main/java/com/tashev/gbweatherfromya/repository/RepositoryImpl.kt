package com.tashev.gbweatherfromya.repository

import com.tashev.gbweatherfromya.dataSource.Weather

class RepositoryImpl : Repository {

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorage(): Weather {
        return Weather()
    }
}