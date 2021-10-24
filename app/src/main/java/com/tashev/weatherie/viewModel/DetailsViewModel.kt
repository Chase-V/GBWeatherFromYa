package com.tashev.weatherie.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tashev.weatherie.MyApp.Companion.getHistoryDAO
import com.tashev.weatherie.dataSource.Weather
import com.tashev.weatherie.repository.DetailsRepository
import com.tashev.weatherie.repository.DetailsRepositoryImpl
import com.tashev.weatherie.repository.LocalRepositoryImpl
import com.tashev.weatherie.repository.weatherLoaderAndDTO.RemoteDataSource
import com.tashev.weatherie.repository.weatherLoaderAndDTO.WeatherDTO
import com.tashev.weatherie.utils.convertDTOtoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(
    private val detailsLiveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource()),
    private val historyRepositoryImpl: LocalRepositoryImpl = LocalRepositoryImpl(getHistoryDAO())
) : ViewModel() {

    fun saveWeather(weather: Weather){
        historyRepositoryImpl.saveEntity(weather)
    }

    fun getLiveData() = detailsLiveDataToObserve

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveDataToObserve.value = AppState.Loading
        detailsRepositoryImpl.getWeatherFromRemoteServer(lat, lon, callback)
    }

    private val callback = object : Callback<WeatherDTO> {
        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {

            if (response.isSuccessful&&response.body()!=null){
                val weatherDTO = response.body()
                weatherDTO?.let {
                    detailsLiveDataToObserve.postValue(AppState.Success(convertDTOtoModel(weatherDTO)))
                }
            }
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
// TODO           Snackbar.make(, R.string.errorLoadingFromServer, Snackbar.LENGTH_INDEFINITE)
        }

    }
}