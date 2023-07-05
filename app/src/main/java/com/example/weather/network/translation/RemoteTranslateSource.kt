package com.example.weather.network.translation

import android.view.translation.TranslationResponse
import kotlinx.coroutines.flow.Flow

interface RemoteTranslateSource {
    suspend fun translateText(text: String): Flow<TranslationResponse>
}