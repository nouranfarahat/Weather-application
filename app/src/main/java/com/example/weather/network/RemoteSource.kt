package com.example.weather.network

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

interface RemoteSource {
    suspend fun getWeatherFromNetwork(): Flow<Gson>
}