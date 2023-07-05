package com.example.weather.network.translation

import com.example.weather.utilities.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TranslateRetrofitHelper {

    private const val TAG = "TRANSLATE_CLIENT"

    var retrofitInstance= Retrofit.Builder()
        .baseUrl(Constants.TRANSLATE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
