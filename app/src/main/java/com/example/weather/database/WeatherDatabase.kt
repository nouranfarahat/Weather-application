package com.example.products

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weather.database.DataConverter
import com.example.weather.model.AlertPojo
import com.example.weather.model.FavoriteWeather
import com.example.weather.model.WeatherResponse

@Database(entities = [FavoriteWeather::class,AlertPojo::class,WeatherResponse::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class WeatherDatabase:RoomDatabase() {
    abstract fun getFavWeatherDao():FavoriteWeatherDAO
    abstract fun getWeatherAlertDao():WeatherAlertDAO
    abstract fun getWeatherDao():WeatherDAO

    companion object{
        @Volatile
        private var INSTANCE:WeatherDatabase?=null

        fun getInstance(context: Context):WeatherDatabase
        {
            return INSTANCE?: synchronized(this){
                val instance= Room.databaseBuilder(context.applicationContext,WeatherDatabase::class.java,"Weather_Database")
                    .build()
                INSTANCE=instance

            instance}
        }
    }

}