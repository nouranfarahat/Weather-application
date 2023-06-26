package com.example.weather.network

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductClient:RemoteSource {

        val productService: ProductService by lazy {
            RetrofitHelper.retrofitInstance.create(ProductService::class.java)
        }
    companion object
    {
        private var instance:ProductClient?=null
        fun getInstance(): ProductClient {
            return instance?: synchronized(this){
                val temp=ProductClient()
                instance=temp
                temp
            }
        }

    }

    override suspend fun getAllProductFromNetwork(): Flow<Gson> = flow {
        val productList= productService.getAllProducts().body()?:" "
        emit(productList as Gson)
    } //leh mktbtsh hna viewModelScope


}