package com.tashev.weatherie.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tashev.weatherie.MyApp
import com.tashev.weatherie.repository.LocalRepositoryImpl

class HistoryViewModel(
    private val historyLiveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepositoryImpl: LocalRepositoryImpl = LocalRepositoryImpl(MyApp.getHistoryDAO())
) : ViewModel() {

    fun getAllHistory() {
        historyLiveDataToObserve.value = AppState.Loading
        Thread{
            historyLiveDataToObserve.postValue(AppState.Success(historyRepositoryImpl.getAllHistory()))
        }.start()
    }

    fun getLiveData() = historyLiveDataToObserve
}