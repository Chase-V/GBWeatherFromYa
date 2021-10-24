package com.tashev.weatherie.dataSource

import android.os.Parcelable
import com.tashev.weatherie.MyApp
import com.tashev.weatherie.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val weatherFact: WeatherFact = WeatherFact(),
    val weatherForecast: WeatherForecast = WeatherForecast()
) : Parcelable

@Parcelize
data class WeatherFact(
    val city: City = getDefaultCity(),
    val temperature: Int = 25,
    val feelsLike: Int = 18,
    val windSpeed: Double = 5.0,
    val humidity: Int = 54,
    val condition: String = "Clear",
    val pressure: Int = 55,
    val icon: String = "skc_n"
) : Parcelable

@Parcelize
data class WeatherForecast(
    val parts: List<Parts> = listOf(Parts())
) : Parcelable

@Parcelize
data class Parts(
    val partName: String = "day",
    val tempMin: Int = 20,
    val tempAvg: Int = 25,
    val tempMax: Int = 30,
    val windSpeed: Double = 3.0,
    val humidity: Long = 55,
    val icon: String = "skc_n",
    val condition: String = "Clear",
    val feelsLike: Long = 22,
) : Parcelable

fun getDefaultCity() = City("Sevastopol", 44.61649704, 33.52513123)

fun getWorldCities(): List<Weather> = listOf(
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.lnd), 51.5085300, -0.1257400), 1, 2)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.tyo), 35.6895000, 139.6917100), 3, 4)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.prs), 48.8534100, 2.3488000), 5, 6)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.bln), 52.52000659999999, 13.404953999999975), 7, 8)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.rom), 41.9027835, 12.496365500000024), 9, 10)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.mnk), 53.90453979999999, 27.561524400000053), 11, 12)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.sml), 41.0082376, 28.97835889999999), 13, 14)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.wsn), 38.9071923, -77.03687070000001), 15, 16)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.kyv), 50.4501, 30.523400000000038), 17, 18)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.bjn), 39.90419989999999, 116.40739630000007), 19, 20))
)

fun getRussianCities(): List<Weather> = listOf(
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.svl), 44.61649704, 33.52513123), 25, 22)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.msc), 55.755826, 37.617299900000035), 1, 2)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.spb), 59.9342802, 30.335098600000038), 3, 3)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.nsb), 55.00835259999999, 82.93573270000002), 5, 6)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.ekb), 56.83892609999999, 60.60570250000001), 7, 8)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.nnv), 56.2965039, 43.936059), 9, 10)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.kzn), 55.8304307, 49.06608060000008), 11, 12)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.clb), 55.1644419, 61.4368432), 13, 14)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.omsk), 54.9884804, 73.32423610000001), 15, 16)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.rnd), 47.2357137, 39.701505), 17, 18)),
    Weather(WeatherFact(City(MyApp.applicationContext().resources.getString(R.string.ufa), 54.7387621, 55.972055400000045), 19, 20))
)
