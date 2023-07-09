package com.example.mvvm.database

import android.content.Context
import com.example.products.FavoriteWeatherDAO
import com.example.products.WeatherDatabase
import com.example.weather.model.FavoriteWeather
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource(context: Context) : LocalSource {

    private val dao: FavoriteWeatherDAO by lazy {
        val database: WeatherDatabase = WeatherDatabase.getInstance(context)
        database.getFavWeatherDao()
    }

    override suspend fun insertFavWeather(weather: FavoriteWeather) {
        dao.insertFavWeather(weather)
    }

    override suspend fun removeFavWeather(weather: FavoriteWeather) {
        dao.deleteFavWeather(weather)
    }

    override suspend fun getFavWeatherList(): Flow<List<FavoriteWeather>> {
        return dao.getWeatherList()
    }


}