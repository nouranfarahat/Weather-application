package com.example.mvvm.database

import androidx.lifecycle.LiveData
import com.example.weather.model.FavoriteWeather
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    suspend fun insertFavWeather(weather: FavoriteWeather)
    suspend fun removeFavWeather(weather: FavoriteWeather)
    suspend fun getFavWeatherList(): Flow<List<FavoriteWeather>>
}