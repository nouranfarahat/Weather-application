package com.example.weather.network


import retrofit2.Response
import retrofit2.http.GET

interface WeatherService {
    @GET("onecall")
    suspend fun getWeatherInfo(): Response<WeatherResponse>
}