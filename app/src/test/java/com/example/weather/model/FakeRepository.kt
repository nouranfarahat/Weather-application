package com.example.weather.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository(private val remoteSource: FakeRemoteSource,private val localSource: FakeLocalSource):RepositoryInterface {
    override suspend fun getWeatherResponse(
        latitude: Double,
        longtude: Double,
        language: String,
        unit: String
    ): Flow<WeatherResponse> {
        return flowOf(remoteSource.weatherResponse)
    }

    override suspend fun insertWeatherToFav(weather: FavoriteWeather) {
        localSource.insertFavWeather(weather)
    }

    override suspend fun removeWeatherFromFav(weather: FavoriteWeather) {
        localSource.removeFavWeather(weather)
    }

    override suspend fun getFavWeatherList(): Flow<List<FavoriteWeather>> {
        return localSource.getFavWeatherList()
    }

    override suspend fun insertAlertToList(alert: AlertPojo) {
        localSource.insertWeatherAlert(alert)
    }

    override suspend fun removeAlertFromList(alert: AlertPojo) {
        localSource.removeWeatherAlert(alert)
    }

    override suspend fun getWeatherAlertList(): Flow<List<AlertPojo>> {
        return localSource.getWeatherAlertList()
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        localSource.insertWeather(weatherResponse)
    }

    override suspend fun removeWeather(weatherResponse: WeatherResponse) {
        localSource.removeWeather(weatherResponse)
    }

    override suspend fun getWeather(): Flow<List<WeatherResponse>> {
        return localSource.getWeather()
    }
}