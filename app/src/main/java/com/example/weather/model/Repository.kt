package com.example.weather.model

import android.view.translation.TranslationResponse
import com.example.mvvm.database.LocalSource
import com.example.weather.network.RemoteSource
import com.example.weather.model.WeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class Repository(
    var remoteSource: RemoteSource,
    var localSource: LocalSource
):RepositoryInterface{

    companion object
    {
        private var instance: Repository?=null
        fun getInstance(
            remoteSource: RemoteSource, localSource: LocalSource
        ): Repository {
            return instance?: synchronized(this){
                val temp= Repository(remoteSource,localSource)
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

    override suspend fun insertWeatherToFav(weather: FavoriteWeather) {
        localSource.insertFavWeather(weather)
    }

    override suspend fun removeWeatherFromFav(weather: FavoriteWeather) {
        localSource.removeFavWeather(weather)
    }

    override suspend fun getFavWeatherList(): Flow<List<FavoriteWeather>> {
        return localSource.getFavWeatherList()
    }

    override suspend fun insertAlertToList(alert: AlertPojo) {
        localSource.insertWeatherAlert(alert)
    }

    override suspend fun removeAlertFromList(alert: AlertPojo) {
        localSource.removeWeatherAlert(alert)
    }

    override suspend fun getWeatherAlertList(): Flow<List<AlertPojo>> {
        return localSource.getWeatherAlertList()
    }

}