package com.example.weather.utilities

import com.example.weather.model.WeatherResponse

sealed class WeatherState {
    class Success(val data: List<WeatherResponse>):WeatherState()
    class Failure(val msg: Throwable):WeatherState()
    object Loading:WeatherState()
}