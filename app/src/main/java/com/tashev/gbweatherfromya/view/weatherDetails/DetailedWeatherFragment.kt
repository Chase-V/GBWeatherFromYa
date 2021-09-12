package com.tashev.gbweatherfromya.view.weatherDetails

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.android.material.snackbar.Snackbar
import com.tashev.gbweatherfromya.R
import com.tashev.gbweatherfromya.dataSource.Weather
import com.tashev.gbweatherfromya.databinding.DetailedWeatherFragmentBinding
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.WeatherDTO
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.WeatherLoader
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.WeatherLoaderListener
import kotlinx.android.synthetic.main.detailed_weather_fragment.*

//import kotlinx.android.synthetic.main.for_test_detailed_weather_fragment.*

class DetailedWeatherFragment : Fragment(), WeatherLoaderListener {

    companion object {

        const val BUNDLE_KEY = "weather"

        fun newInstance(bundle: Bundle): DetailedWeatherFragment {
            val fragment = DetailedWeatherFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: DetailedWeatherFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailedWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val localWeather: Weather by lazy {
        (arguments?.getParcelable(BUNDLE_KEY)) ?: Weather()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        WeatherLoader(this, localWeather.city.lat, localWeather.city.lon).loadWeather()
    }

    private fun showWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            cityName.text = localWeather.city.name
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                localWeather.city.lat.toString(),
                localWeather.city.lon.toString()
            )

            temperatureValue.text = String.format("%s°", weatherDTO.fact.temp.toString())
            condition.text = translations[weatherDTO.fact.condition]
            feelsLikeValue.text = String.format("%s°", weatherDTO.fact.feels_like.toString())
            windSpeed.text = String.format("%s м/c", weatherDTO.fact.wind_speed.toString())
            humidity.text = String.format("%s%%", weatherDTO.fact.humidity.toString())
            weatherConditionIcon.loadImageFromUrl("https://yastatic.net/weather/i/icons/funky/dark/${weatherDTO.fact.icon}.svg")

//region мб перекручу это на инфлейтер с заготовленным контейнером
            with(weatherDTO.forecast.parts[0]) {
                forecastPartName.text = translations[part_name]
                forecastCondition.text = translations[condition]
                forecastHumidity.text = String.format("%s%%", humidity.toString())
                forecastWindSpeed.text = String.format("%s м/c", wind_speed.toString())
                forecastTempMin.text = String.format("%s°", temp_min.toString())
                forecastTempAvg.text = String.format("%s°", temp_avg.toString())
                forecastTempMax.text = String.format("%s°", temp_max.toString())
                forecastFeelsLike.text = String.format("%s°", feels_like.toString())
                forecastConditionIcon.loadImageFromUrl("https://yastatic.net/weather/i/icons/funky/dark/${icon}.svg")
            }
            with(weatherDTO.forecast.parts[1]) {
                forecastPartName2.text = translations[part_name]
                forecastCondition2.text = translations[condition]
                forecastHumidity2.text = String.format("%s%%", humidity.toString())
                forecastWindSpeed2.text = String.format("%s м/c", wind_speed.toString())
                forecastTempMin2.text = String.format("%s°", temp_min.toString())
                forecastTempAvg2.text = String.format("%s°", temp_avg.toString())
                forecastTempMax2.text = String.format("%s°", temp_max.toString())
                forecastFeelsLike2.text = String.format("%s°", feels_like.toString())
                forecastConditionIcon2.loadImageFromUrl("https://yastatic.net/weather/i/icons/funky/dark/${icon}.svg")
            }
//endregion
        }
    }

    private val translations = mapOf(
        "clear" to "Ясно",
        "partly-cloudy" to "Малооблачно",
        "cloudy" to "Облачно с прояснениями",
        "overcast" to "Пасмурно",
        "drizzle" to "Морось",
        "light-rain" to "Небольшой дождь",
        "rain" to "Дождь",
        "moderate-rain" to "Умеренно сильный дождь",
        "heavy-rain" to "Сильный дождь",
        "continuous-heavy-rain" to "Длительный сильный дождь",
        "showers" to "Ливень",
        "wet-snow" to "Дождь со снегом",
        "light-snow" to "Небольшой снег",
        "snow" to "Снег",
        "snow-showers" to "Снегопад",
        "hail" to "Град",
        "thunderstorm" to "Гроза",
        "thunderstorm-with-rain" to "Дождь с грозой",
        "thunderstorm-with-hail" to "Гроза с градом",
        "night" to "Ночь",
        "morning" to "Утро",
        "day" to "День",
        "evening" to "вечер"
    )

    private fun ImageView.loadImageFromUrl(imageUrl: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry {
                add(SvgDecoder(this@loadImageFromUrl.context))
            }
            .build()

        val imageRequest = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(300)
            .data(imageUrl)
            .target(
                onStart = {
                    this.setImageResource(R.drawable.sunny)
                },
                onSuccess = { result ->
                    val bitmap = (result as BitmapDrawable).bitmap
                    this.setImageBitmap(bitmap)
                    //dismiss the loader if any
                },
                onError = {
                    this.setImageResource(R.drawable.sunny)
                }
            )
            .build()

        imageLoader.enqueue(imageRequest)
    }

    override fun onLoaded(weatherDTO: WeatherDTO) {
        showWeather(weatherDTO)
    }

    override fun onFailed(throwable: Throwable) {
        Snackbar.make(binding.root, getString(R.string.error), Snackbar.LENGTH_INDEFINITE).show()
    }
}