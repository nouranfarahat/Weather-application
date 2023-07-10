package com.example.weather.utilities

import com.example.weather.model.AlertPojo

sealed class AlertState
{
    class Success(val data: List<AlertPojo>):AlertState()
    class Failure(val msg: Throwable):AlertState()
    object Loading:AlertState()
}
