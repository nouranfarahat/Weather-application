package com.example.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather.utilities.Constants

@Entity(tableName = "Weather_Alert")
data class AlertPojo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startDate: Long,
    val endDate: Long,
    val startTime: Long,
    val endTime: Long,
    val timezone:String,
    val fullStartTime:Long,
    val fullEndTime:Long,
    val type:String=Constants.ALARM,
    var description:String="The Weather is fine"
)