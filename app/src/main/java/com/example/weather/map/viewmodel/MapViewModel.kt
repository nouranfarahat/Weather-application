package com.example.weather.map.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.FavoriteWeather
import com.example.weather.model.RepositoryInterface
import com.example.weather.utilities.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MapViewModel(private val repo: RepositoryInterface) : ViewModel() {

    fun insertFavWeather(favoriteWeather: FavoriteWeather)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWeatherToFav(favoriteWeather)
        }
    }
}