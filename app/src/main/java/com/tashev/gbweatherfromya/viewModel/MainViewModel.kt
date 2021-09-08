package com.tashev.gbweatherfromya.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeather() = getDataFromRemoteSource()

    private fun getDataFromRemoteSource() {
        liveDataToObserve.postValue(AppState.Loading)
        Thread {
            sleep(2000)
            liveDataToObserve.postValue(AppState.Success(Any()))
        }.start()
    }

}