package com.example.weather.model

import com.example.products.FavoriteWeatherDAO
import com.example.weather.utilities.AlertState
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {

    suspend fun getWeatherResponse(
        latitude: Double,
        longtude: Double,
        language: String = "en",
        unit: String = "standard"
    ): Flow<WeatherResponse>

    suspend fun insertWeatherToFav(weather: FavoriteWeather)
    suspend fun removeWeatherFromFav(weather: FavoriteWeather)
    suspend fun getFavWeatherList(): Flow<List<FavoriteWeather>>

    suspend fun insertAlertToList(alert: AlertPojo)
    suspend fun removeAlertFromList(alert: AlertPojo)
    suspend fun getWeatherAlertList(): Flow<List<AlertPojo>>

}