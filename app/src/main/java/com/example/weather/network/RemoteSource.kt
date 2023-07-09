package com.example.weather.network

import com.example.weather.model.WeatherResponse
import com.example.weather.utilities.Constants
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface RemoteSource {
    suspend fun getWeatherFromNetwork(latitude:Double,
                                      longtude:Double,
                                      language:String,
                                      unit:String): Flow<WeatherResponse>
}