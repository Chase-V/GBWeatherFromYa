package com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

const val DETAILS_INTENT_FILTER = "DETAILS_INTENT_FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val LATITUDE_EXTRA = "LATITUDE"
const val LONGITUDE_EXTRA = "LONGITUDE"

class DetailService(name:String = "details") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = it.getDoubleExtra(LATITUDE_EXTRA, -1.0)
            val lon = it.getDoubleExtra(LONGITUDE_EXTRA, -1.0)
            loadWeather(lat, lon)
        }
    }

    private val API_KEY: String = "948e25d8-98e9-43cc-a59b-0a36aab31239"
    private val API_KEY_TYPE: String = "X-Yandex-API-Key"

    private fun loadWeather(lat:Double, lon:Double) {
        val url = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}&lang=ru_RU")

        Thread {
            val urlConnection = url.openConnection() as HttpsURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.addRequestProperty(API_KEY_TYPE, API_KEY)
            urlConnection.readTimeout = 10000
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val weatherDTO = Gson().fromJson(reader, WeatherDTO::class.java)
            val handler = Handler(Looper.getMainLooper())
//            if (weatherDTO != null) {
//                handler.post { listener.onLoaded(weatherDTO) }
//            } else {
//                handler.post { listener.onFailed(Throwable("Weather is null")) }
//            }

            val mySendIntent = Intent(DETAILS_INTENT_FILTER)
            mySendIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, weatherDTO)
            LocalBroadcastManager.getInstance(this).sendBroadcast(mySendIntent)

            urlConnection.disconnect()
        }.start()
    }
}