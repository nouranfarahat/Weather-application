package com.example.weather.database

import androidx.room.TypeConverter
import com.example.weather.model.Alerts
import com.example.weather.model.Current
import com.example.weather.model.Daily
import com.example.weather.model.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DataConverter {
    @TypeConverter
    fun fromCurrentToString(current: Current): String {
        return Gson().toJson(current)
    }

    @TypeConverter
    fun fromStringToCurrent(currentStr: String): Current {
        val type = object : TypeToken<Current>() {}.type
        return Gson().fromJson(currentStr, type)
    }
    @TypeConverter
    fun fromHourlyListToString(hourly: List<Hourly>): String {
        return Gson().toJson(hourly)
    }

    @TypeConverter
    fun fromStringToHourlyList(hourlyStr: String): List<Hourly> {
        val type = object : TypeToken<List<Hourly>>() {}.type
        return Gson().fromJson(hourlyStr, type)
    }

    @TypeConverter
    fun fromDailyListToString(daily: List<Daily>): String {
        return Gson().toJson(daily)
    }

    @TypeConverter
    fun fromStringToDailyList(dailyStr: String): List<Daily> {
        val type = object : TypeToken<List<Daily>>() {}.type
        return Gson().fromJson(dailyStr, type)
    }
    @TypeConverter
    fun fromAlertListToString(alert: List<Alerts>): String {
        return Gson().toJson(alert)
    }

    @TypeConverter
    fun fromStringToAlertList(alertStr: String): List<Alerts> {
        val type = object : TypeToken<List<Alerts>>() {}.type
        return Gson().fromJson(alertStr, type)
    }
}
