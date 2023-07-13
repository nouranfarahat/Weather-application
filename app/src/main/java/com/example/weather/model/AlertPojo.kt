package com.example.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Weather_Alert")
data class AlertPojo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startDate: Long,
    val endDate: Long,
    val startTime: Long,
    val endTime: Long,
    val timezone:String,
    val description:String
)