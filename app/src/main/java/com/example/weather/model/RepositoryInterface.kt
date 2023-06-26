package com.example.weather.model

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {

    suspend fun getProductsList(): Flow<Gson>
}