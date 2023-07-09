package com.example.weather.utilities

import com.example.weather.model.FavoriteWeather
import com.example.weather.model.WeatherResponse

sealed class FavState
{
    class Success(val data: List<FavoriteWeather>):FavState()
    class Failure(val msg: Throwable):FavState()
    object Loading:FavState()
}
