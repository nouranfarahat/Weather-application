package com.example.weather.network.translation

import android.view.translation.TranslationResponse
import com.example.weather.utilities.Constants
import retrofit2.http.POST
import retrofit2.http.Query

interface TranslateService {
    @POST("/language/translate/v2")
    suspend fun translateText(
        @Query("q") text: String,
        @Query("target") targetLanguage: String=Constants.ARABIC,
        @Query("key") apiKey: String=Constants.GOOGLE_API_KEY
    ): TranslationResponse
}