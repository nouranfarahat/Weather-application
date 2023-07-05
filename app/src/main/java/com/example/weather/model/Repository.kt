package com.example.weather.model

import android.view.translation.TranslationResponse
import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherResponse
import com.example.weather.network.translation.RemoteTranslateSource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class Repository(
    var remoteSource: RemoteSource,
    var remoteTranslateSource:RemoteTranslateSource
):RepositoryInterface{

    companion object
    {
        private var instance: Repository?=null
        fun getInstance(
            remoteSource: RemoteSource,
            remoteTranslateSource: RemoteTranslateSource
        ): Repository {
            return instance?: synchronized(this){
                val temp= Repository(remoteSource,remoteTranslateSource)
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

    override suspend fun getTranslatedText(text: String): Flow<TranslationResponse> {
        return remoteTranslateSource.translateText(text)
    }


}