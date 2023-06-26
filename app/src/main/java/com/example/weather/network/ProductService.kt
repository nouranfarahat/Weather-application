package com.example.weather.network


import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {
    @GET("onecall")
    suspend fun getAllProducts(): Response<Gson>
}