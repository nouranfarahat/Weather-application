package com.example.weather.utilities

import android.view.translation.TranslationResponse
import com.example.weather.network.WeatherResponse

sealed class TranslateState {
    class Success(val text: TranslationResponse):TranslateState()
    class Failure(val msg: Throwable):TranslateState()
    object Loading:TranslateState()
}