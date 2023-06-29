package com.example.weather.network


import com.example.weather.utilities.Constants
import org.intellij.lang.annotations.Language
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("onecall")
    suspend fun getWeatherInfo(@Query("lat") latitude:Double,
                               @Query("lon") longtude:Double,
                               @Query("lang") language:String="en",
                               @Query("units") unit:String="standard",
                               @Query("exclude") miutely:String="minutely",
                               @Query("appid") key:String=Constants.API_KEY
    ): Response<WeatherResponse>
}