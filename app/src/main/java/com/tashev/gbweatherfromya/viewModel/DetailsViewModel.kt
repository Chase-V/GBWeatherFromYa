package com.tashev.gbweatherfromya.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tashev.gbweatherfromya.repository.DetailsRepository
import com.tashev.gbweatherfromya.repository.DetailsRepositoryImpl
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.RemoteDataSource
import com.tashev.gbweatherfromya.repository.weatherLoaderAndDTO.WeatherDTO
import com.tashev.gbweatherfromya.utils.convertDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(
    private val detailsLiveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())
) : ViewModel() {

    fun getLiveData() = detailsLiveDataToObserve

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveDataToObserve.value = AppState.Loading
        detailsRepositoryImpl.getWeatherFromRemoteServer(lat, lon, callback)
    }

    private val callback = object : Callback<WeatherDTO> {
        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: String = response.body().toString()
            if (response.isSuccessful && serverResponse != null) {
                val weatherDTO = Gson().fromJson(serverResponse, WeatherDTO::class.java)
                detailsLiveDataToObserve.value = AppState.Success(convertDTO(weatherDTO))
            }
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
// TODO           Snackbar.make(, R.string.errorLoadingFromServer, Snackbar.LENGTH_INDEFINITE)
        }

    }

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        detailsLiveDataToObserve.postValue(AppState.Loading)
        detailsLiveDataToObserve.postValue(
            AppState.Success(
                if (isRussian) detailsRepositoryImpl.getWeatherFromLocalStorageRus()
                else detailsRepositoryImpl.getWeatherFromLocalStorageWorld()
            )
        )
    }
}