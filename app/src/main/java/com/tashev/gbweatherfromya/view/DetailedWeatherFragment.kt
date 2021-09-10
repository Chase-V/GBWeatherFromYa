package com.tashev.gbweatherfromya.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tashev.gbweatherfromya.R
import com.tashev.gbweatherfromya.dataSource.Weather
import com.tashev.gbweatherfromya.databinding.DetailedWeatherFragmentBinding

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailedWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { setData((it.getParcelable(BUNDLE_KEY))?: Weather()) }
    }

    private fun setData(weather: Weather){
        with (binding) {
            cityName.text = weather.city.city
            cityCoordinates.text = String.format(getString(R.string.city_coordinates),weather.city.lat.toString(),weather.city.lon.toString())
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
        }
    }
}