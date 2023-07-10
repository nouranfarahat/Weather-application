package com.example.products

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.model.AlertPojo
import com.example.weather.model.FavoriteWeather

@Database(entities = [FavoriteWeather::class,AlertPojo::class], version = 1)
abstract class WeatherDatabase:RoomDatabase() {
    abstract fun getFavWeatherDao():FavoriteWeatherDAO
    abstract fun getWeatherAlertDao():WeatherAlertDAO
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