package com.example.mvvm.allproducts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.AlertPojo
import com.example.weather.model.FavoriteWeather
import com.example.weather.model.RepositoryInterface
import com.example.weather.model.WeatherResponse
import com.example.weather.utilities.ApiState
import com.example.weather.utilities.FavState
import com.example.weather.utilities.WeatherState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val mutableWeather : MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val weatherResponse: StateFlow<ApiState>
        get() = mutableWeather

    private val mutableCurrentWeather: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val weatherCurrenrt: StateFlow<ApiState>
        get() = mutableCurrentWeather

    /*init {
        getLocationWeather() //law m3mltsh de w kant getLocalProduct public hnadyha bs fe el activity?
    }*/
    fun getLocationWeather(latitude: Double,
                           longtude: Double,
                           language: String="en",
                           unit: String="standard")= viewModelScope.launch {

        repo.getWeatherResponse(latitude, longtude, language, unit)
            .catch {
                    e-> mutableWeather.value=ApiState.Failure(e)
                Log.i("TAG", "getLocationWeather: Catch")
            }
            .collect{
                    data-> mutableWeather.value=ApiState.Success(data)
                Log.i("TAG", "getLocationWeather: Collect")

            } //hal hwa hyrg3 lw7do ll default Dispatcher b3d ma y5ls?
    }


    fun insertWeatherToRoom(weatherResponse: WeatherResponse)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWeather(weatherResponse)
            //getLocalAlert()
        }
    }
    fun getCurrentWeather() = viewModelScope.launch(Dispatchers.IO) {

        repo.getWeather()
            .catch {   e-> mutableCurrentWeather.value= ApiState.Failure(e)
                Log.i("TAG", "getLocationWeather: offline Catch")
            }
            .collect{
                    data-> mutableCurrentWeather.value= ApiState.Success(data.get(0))
                Log.i("TAG", "getLocationWeather: offline Collect")

            }

    }



    /*private fun getLocalProducts() {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getProductsList()
                .catch { e-> mutableProducts.value=ApiState.Failure(e) }
                .collect{
                    data-> mutableProducts.value=ApiState.Success(data)
            } //hal hwa hyrg3 lw7do ll default Dispatcher b3d ma y5ls?
        }

    }*/




}