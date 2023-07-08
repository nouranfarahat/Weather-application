package com.example.weather.utilities

import com.example.weather.network.WeatherResponse


sealed class ApiState
{
    class Success(val data: WeatherResponse):ApiState()
    class Failure(val msg: Throwable):ApiState()
    object Loading:ApiState()

}
