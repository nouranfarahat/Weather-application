package com.example.weather.network

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherClient:RemoteSource {

        val productService: WeatherService by lazy {
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


    override suspend fun getWeatherFromNetwork(): Flow<Gson> = flow {
        val productList= productService.getWeatherInfo().body()?:" "
        emit(productList as Gson)
    }


}