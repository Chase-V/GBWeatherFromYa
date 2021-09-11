package com.tashev.gbweatherfromya.repository


import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val listener: WeatherLoaderListener, private val lat: Double, private val lon: Double) {
    private val API_KEY: String = "948e25d8-98e9-43cc-a59b-0a36aab31239"
    private val API_KEY_TYPE: String = "X-Yandex-API-Key"

    fun loadWeather() {
        val url = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}&lang=ru_RU")

        Thread {
            val urlConnection = url.openConnection() as HttpsURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.addRequestProperty(API_KEY_TYPE, API_KEY)
            urlConnection.readTimeout = 10000
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val weatherDTO = Gson().fromJson(reader, WeatherDTO::class.java)
            val handler = Handler(Looper.getMainLooper())
            handler.post { listener.onLoaded(weatherDTO) }
            urlConnection.disconnect()
        }.start()
    }
}

interface WeatherLoaderListener {
    fun onLoaded(weatherDTO: WeatherDTO)
    fun onFailed(throwable: Throwable)
}