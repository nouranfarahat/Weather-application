package com.example.weather.alert.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.AlertPojo
import com.example.weather.model.FavoriteWeather
import com.example.weather.model.RepositoryInterface
import com.example.weather.utilities.AlertState
import com.example.weather.utilities.FavState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel (private val repo: RepositoryInterface) : ViewModel() {

    private val mutableWeatherAlert: MutableStateFlow<AlertState> = MutableStateFlow(AlertState.Loading)
    val weatherAlertResponse: StateFlow<AlertState>
        get() = mutableWeatherAlert

    init {
        getLocalAlert()
    }

    private fun getLocalAlert() = viewModelScope.launch(Dispatchers.IO) {

        repo.getWeatherAlertList()
            .catch { e ->
                mutableWeatherAlert.value = AlertState.Failure(e)
                Log.i("TAG", "getLocalWeather: Fav catch")
            }
            .collect { data ->
                mutableWeatherAlert.value = AlertState.Success(data)
                Log.i("TAG", "getLocalWeather: Fav collect")
            }

    }

    fun deleteAlertFromList(alertPojo: AlertPojo) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeAlertFromList(alertPojo)
            getLocalAlert()
        }
    }


}
