package com.example.products

import androidx.room.*
import com.example.weather.model.AlertPojo
import com.example.weather.model.FavoriteWeather
import com.example.weather.model.WeatherResponse
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

@Dao
interface WeatherAlertDAO {

    @Query("SELECT * FROM Weather_Alert")
    fun getAlertList(): Flow<List<AlertPojo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherAlert(alert: AlertPojo?):Long

    @Delete
    suspend fun deleteWeatherAlert(alert: AlertPojo)
}
@Dao
interface WeatherDAO {

    @Query("SELECT * FROM Weather_Table")
    fun getWeather(): Flow<List<WeatherResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherResponse: WeatherResponse?):Long

    @Delete
    suspend fun deleteWeather(weatherResponse: WeatherResponse)
}