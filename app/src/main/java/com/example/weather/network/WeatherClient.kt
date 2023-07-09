package com.example.weather.network

import com.example.weather.model.WeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherClient:RemoteSource {

        val weatherService: WeatherService by lazy {
            RetrofitHelper.retrofitInstance.create(WeatherService::class.java)
        }
    companion object
    {
        private var instance:WeatherClient?=null
        fun getInstance(): WeatherClient {
            return instance?: synchronized(this){
                val temp=WeatherClient()
                instance=temp
                temp
            }
        }

    }

    override suspend fun getWeatherFromNetwork(
        latitude: Double,
        longtude: Double,
        language: String,
        unit: String
    ): Flow<WeatherResponse> = flow{
        val currentWeather=weatherService.getWeatherInfo(latitude,longtude, language, unit).body()
        if (currentWeather != null) {
            emit(currentWeather)
        }
    }


}