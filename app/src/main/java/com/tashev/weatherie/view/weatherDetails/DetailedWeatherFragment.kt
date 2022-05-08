package com.tashev.weatherie.view.weatherDetails

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.android.material.snackbar.Snackbar
import com.tashev.weatherie.R
import com.tashev.weatherie.dataSource.Weather
import com.tashev.weatherie.dataSource.WeatherFact
import com.tashev.weatherie.databinding.DetailedWeatherFragmentBinding


import com.tashev.weatherie.viewModel.AppState
import com.tashev.weatherie.viewModel.DetailsViewModel

class DetailedWeatherFragment : Fragment() {

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
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

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
        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        getWeather()
    }

    private fun getWeather() {
        viewModel.getWeatherFromRemoteSource(
            localWeather.weatherFact.city.lat,
            localWeather.weatherFact.city.lon
        )
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val weather = appState.weatherData
                showWeather(weather[0])
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.INVISIBLE
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.INVISIBLE
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                Snackbar.make(binding.root, R.string.error, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun showWeather(weather: Weather) {
        viewModel.saveWeather(
            Weather(
                WeatherFact(
                    localWeather.weatherFact.city,
                    weather.weatherFact.temperature,
                    weather.weatherFact.feelsLike,
                    weather.weatherFact.windSpeed,
                    weather.weatherFact.humidity,
                    weather.weatherFact.condition,
                    weather.weatherFact.pressure,
                    weather.weatherFact.icon
                )
            )
        )
        with(binding) {
            cityName.text = localWeather.weatherFact.city.name
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                localWeather.weatherFact.city.lat.toString(),
                localWeather.weatherFact.city.lon.toString()
            )
            temperatureValue.text = String.format("%s°", weather.weatherFact.temperature)
            condition.text = getStringResourceByName(weather.weatherFact.condition)
            feelsLikeValue.text = String.format("%s°", weather.weatherFact.feelsLike.toString())
            windSpeed.text = String.format(
                "%s ${resources.getString(R.string.mps)}",
                weather.weatherFact.windSpeed.toString()
            )
            humidity.text = String.format("%s%%", weather.weatherFact.humidity.toString())
            pressure.text = String.format(
                "%s${resources.getString(R.string.mm)}",
                weather.weatherFact.pressure.toString()
            )
            weatherConditionIcon.loadImageFromUrl("https://yastatic.net/weather/i/icons/funky/dark/${weather.weatherFact.icon}.svg")

//region мб перекручу это на инфлейтер с заготовленным контейнером
            with(weather.weatherForecast.parts[0]) {
                forecastPartName.text = getStringResourceByName(partName)
                forecastCondition.text = getStringResourceByName(condition)
                forecastHumidity.text = String.format("%s%%", humidity.toString())
                forecastWindSpeed.text =
                    String.format("%s ${resources.getString(R.string.mps)}", windSpeed.toString())
                forecastTempMin.text = String.format("%s°", tempMin.toString())
                forecastTempAvg.text = String.format("%s°", tempAvg.toString())
                forecastTempMax.text = String.format("%s°", tempMax.toString())
                forecastFeelsLike.text = String.format("%s°", feelsLike.toString())
                forecastConditionIcon.loadImageFromUrl("https://yastatic.net/weather/i/icons/funky/dark/${icon}.svg")
            }
            with(weather.weatherForecast.parts[1]) {
                forecastPartName2.text = getStringResourceByName(partName)
                forecastCondition2.text = getStringResourceByName(condition)
                forecastHumidity2.text = String.format("%s%%", humidity.toString())
                forecastWindSpeed2.text =
                    String.format("%s ${resources.getString(R.string.mps)}", windSpeed.toString())
                forecastTempMin2.text = String.format("%s°", tempMin.toString())
                forecastTempAvg2.text = String.format("%s°", tempAvg.toString())
                forecastTempMax2.text = String.format("%s°", tempMax.toString())
                forecastFeelsLike2.text = String.format("%s°", feelsLike.toString())
                forecastConditionIcon2.loadImageFromUrl("https://yastatic.net/weather/i/icons/funky/dark/${icon}.svg")
            }
//endregion
        }
    }

    private fun getStringResourceByName(aString: String): String {
        var aStringChanged = aString
        if (aString.contains("-", true)) {
            aStringChanged = aString.replace("-", "_")
        }
        val packageName = resources.getResourcePackageName(R.string.clear)
        val resId = resources.getIdentifier(aStringChanged, "string", packageName)
        return getString(resId)
    }

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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}