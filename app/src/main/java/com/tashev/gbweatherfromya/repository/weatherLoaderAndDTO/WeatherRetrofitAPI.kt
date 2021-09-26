package com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO

import com.tashev.gbweatherfromya.utils.YANDEX_API_KEY_NAME
import com.tashev.gbweatherfromya.utils.YANDEX_API_URL_ENDPOINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherRetrofitAPI {

    @GET(YANDEX_API_URL_ENDPOINT)
    fun getWeather(
        @Header(YANDEX_API_KEY_NAME) apikey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<List<WeatherDTO>>

}