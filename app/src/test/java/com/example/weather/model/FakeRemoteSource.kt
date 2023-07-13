package com.example.weather.model

import com.example.weather.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRemoteSource(var weatherResponse: WeatherResponse):RemoteSource {
    override suspend fun getWeatherFromNetwork(
        latitude: Double,
        longtude: Double,
        language: String,
        unit: String
    ): Flow<WeatherResponse> {
        return flowOf(weatherResponse)
    }
}