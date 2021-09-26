package com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO

import com.google.gson.GsonBuilder
import com.tashev.gbweatherfromya.BuildConfig
import com.tashev.gbweatherfromya.utils.YANDEX_API_URL
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {

    private val weatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(YANDEX_API_URL)
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build().create(WeatherRetrofitAPI::class.java)
    }

    fun getWeatherDetails(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
        weatherApi.getWeather(BuildConfig.WEATHER_API_KEY,lat,lon).enqueue(callback)
    }
}