package com.example.weather.model

import com.example.mvvm.database.LocalSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalSource(var favList:MutableList<FavoriteWeather>,var alertList: MutableList<AlertPojo>,var weatherList: MutableList<WeatherResponse>):LocalSource {
    override suspend fun insertFavWeather(weather: FavoriteWeather) {
        favList.add(weather)
    }

    override suspend fun removeFavWeather(weather: FavoriteWeather) {
        favList.remove(weather)
    }

    override suspend fun getFavWeatherList(): Flow<List<FavoriteWeather>> {
        return flowOf(favList)
    }

    override suspend fun insertWeatherAlert(alert: AlertPojo) {
        alertList.add(alert)
    }

    override suspend fun removeWeatherAlert(alert: AlertPojo) {
        alertList.remove(alert)
    }

    override suspend fun getWeatherAlertList(): Flow<List<AlertPojo>> {
        return flowOf(alertList)
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        weatherList.add(weatherResponse)
    }

    override suspend fun removeWeather(weatherResponse: WeatherResponse) {
        weatherList.remove(weatherResponse)
    }

    override suspend fun getWeather(): Flow<List<WeatherResponse>> {
        return flowOf(weatherList)
    }
}