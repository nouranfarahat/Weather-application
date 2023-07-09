package com.example.products

import androidx.room.*
import com.example.weather.model.FavoriteWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteWeatherDAO {

    @Query("SELECT * FROM Favorite_Weather")
    fun getWeatherList(): Flow<List<FavoriteWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavWeather(weather: FavoriteWeather?):Long

    @Delete
    suspend fun deleteFavWeather(weather: FavoriteWeather)
}