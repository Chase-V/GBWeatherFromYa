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

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.postValue(AppState.Loading)
        liveDataToObserve.postValue(
            AppState.Success(
                if (isRussian) repositoryImpl.getWeatherFromLocalStorageRus()
                else repositoryImpl.getWeatherFromLocalStorageWorld()
            )
        )
//        simulateServerResponseWithRandomResult(isRussian)
    }

//    private fun simulateServerResponseWithRandomResult(isRussian: Boolean) {
//        Thread {
//            sleep(800)
//            if (Random.nextInt(15) < 1) {
//                liveDataToObserve.postValue(AppState.Error(Exception("Не удалось загрузить данные о погоде")))
//            } else {
//                liveDataToObserve.postValue(
//                    AppState.Success(
//                        if (isRussian) repositoryImpl.getWeatherFromLocalStorageRus()
//                        else repositoryImpl.getWeatherFromLocalStorageWorld()
//                    )
//                )
//            }
//        }.start()
//    }
}