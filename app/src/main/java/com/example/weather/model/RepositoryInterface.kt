package com.example.weather.model

import android.view.translation.TranslationResponse
import androidx.lifecycle.LiveData
import com.example.weather.network.Current
import com.example.weather.network.WeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {

    suspend fun getWeatherResponse(latitude:Double,
                                   longtude:Double,
                                   language:String="en",
                                   unit:String="standard"): Flow<WeatherResponse>

}