package com.example.mvvm.database

import android.content.Context
import com.example.products.FavoriteWeatherDAO
import com.example.products.WeatherAlertDAO
import com.example.products.WeatherDAO
import com.example.products.WeatherDatabase
import com.example.weather.model.AlertPojo
import com.example.weather.model.FavoriteWeather
import com.example.weather.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource(context: Context) : LocalSource {

    private val favDao: FavoriteWeatherDAO by lazy {
        val database: WeatherDatabase = WeatherDatabase.getInstance(context)
        database.getFavWeatherDao()
    }
    private val alertDao: WeatherAlertDAO by lazy {
        val database: WeatherDatabase = WeatherDatabase.getInstance(context)
        database.getWeatherAlertDao()
    }
    private val weatherDao: WeatherDAO by lazy {
        val database: WeatherDatabase = WeatherDatabase.getInstance(context)
        database.getWeatherDao()
    }

    override suspend fun insertFavWeather(weather: FavoriteWeather) {
        favDao.insertFavWeather(weather)
    }

    override suspend fun removeFavWeather(weather: FavoriteWeather) {
        favDao.deleteFavWeather(weather)
    }

    override suspend fun getFavWeatherList(): Flow<List<FavoriteWeather>> {
        return favDao.getWeatherList()
    }

    override suspend fun insertWeatherAlert(alert: AlertPojo) {
        alertDao.insertWeatherAlert(alert)
    }

    override suspend fun removeWeatherAlert(alert: AlertPojo) {
        alertDao.deleteWeatherAlert(alert)
    }

    override suspend fun getWeatherAlertList(): Flow<List<AlertPojo>> {
        return alertDao.getAlertList()
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        weatherDao.insertWeather(weatherResponse)
    }

    override suspend fun removeWeather(weatherResponse: WeatherResponse) {
        weatherDao.deleteWeather(weatherResponse)
    }

    override suspend fun getWeather(): Flow<List<WeatherResponse>> {
        return weatherDao.getWeather()
    }
}