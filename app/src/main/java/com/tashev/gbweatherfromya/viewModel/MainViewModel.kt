package com.tashev.gbweatherfromya.viewModel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import com.tashev.gbweatherfromya.repository.Repository
import com.tashev.gbweatherfromya.repository.RepositoryImpl
import java.lang.Exception
import java.lang.Thread.sleep
import kotlin.jvm.Throws
import kotlin.random.Random

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSource() = getDataFromLocalSource()
    fun getWeatherFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.postValue(AppState.Loading)
        simulateServerResponce()
    }

    private fun simulateServerResponce() {
        Thread {
            sleep(2000)
            randomResult()
        }.start()
    }

    private fun randomResult() {
        if (Random.nextInt(10) < 4) {
            liveDataToObserve.postValue(AppState.Error(Exception("Не удалось загрузить данные о погоде")))
        } else liveDataToObserve.postValue(AppState.Success(repositoryImpl.getWeatherFromLocalStorage()))
    }

    override fun onCleared() {
        super.onCleared()
    }

}