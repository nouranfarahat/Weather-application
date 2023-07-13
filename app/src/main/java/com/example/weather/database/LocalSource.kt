package com.example.mvvm.database

import androidx.lifecycle.LiveData
import com.example.weather.model.AlertPojo
import com.example.weather.model.FavoriteWeather
import com.example.weather.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    suspend fun insertFavWeather(weather: FavoriteWeather)
    suspend fun removeFavWeather(weather: FavoriteWeather)
    suspend fun getFavWeatherList(): Flow<List<FavoriteWeather>>

    suspend fun insertWeatherAlert(alert: AlertPojo)
    suspend fun removeWeatherAlert(alert: AlertPojo)
    suspend fun getWeatherAlertList(): Flow<List<AlertPojo>>

    suspend fun insertWeather(weatherResponse: WeatherResponse)
    suspend fun removeWeather(weatherResponse: WeatherResponse)
    suspend fun getWeather(): Flow<List<WeatherResponse>>
}