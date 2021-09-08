package com.tashev.gbweatherfromya.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tashev.gbweatherfromya.repository.Repository
import com.tashev.gbweatherfromya.repository.RepositoryImpl
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

//    fun getWeatherFromLocalSource() = getDataFromLocalSource()  Пока что бесполезна, за этим и закомментирована
    fun getWeatherFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.postValue(AppState.Loading)
        simulateServerResponse()
    }

    private fun simulateServerResponse() {
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

}