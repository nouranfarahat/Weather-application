package com.example.weather.network.translation

import android.view.translation.TranslationResponse
import com.example.weather.network.RetrofitHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class TranslateClient:RemoteTranslateSource {
    val translateService: TranslateService by lazy {
        TranslateRetrofitHelper.retrofitInstance.create(TranslateService::class.java)
    }
    companion object
    {
        private var instance: TranslateClient?=null
        fun getInstance(): TranslateClient {
            return instance?: synchronized(this){
                val temp= TranslateClient()
                instance=temp
                temp
            }
        }

    }

    override suspend fun translateText(text: String, ): Flow<TranslationResponse> = flow {
        val response = translateService.translateText(text)
        emit(response)
    }
}