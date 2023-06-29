package com.example.weather.model

import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class Repository(
    var remoteSource: RemoteSource,
):RepositoryInterface{

    companion object
    {
        private var instance: Repository?=null
        fun getInstance(
            remoteSource: RemoteSource,
        ): Repository {
            return instance?: synchronized(this){
                val temp= Repository(remoteSource)
                instance=temp
                temp
            }
        }

    }


    override suspend fun getWeatherResponse(
        latitude: Double,
        longtude: Double,
        language: String,
        unit: String
    ): Flow<WeatherResponse> {
        return remoteSource.getWeatherFromNetwork(latitude, longtude, language, unit)
    }


}