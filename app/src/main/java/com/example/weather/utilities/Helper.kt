package com.example.weather.utilities

import java.text.SimpleDateFormat
import java.util.*

fun convertUnixToDate(unixFormat:Long):Date
{
    return Date(unixFormat*1000L)
}
fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("EEE, d MMMM", Locale.getDefault())
    return dateFormat.format(date)
}
fun formatTime(date: Date): String {
    val timeFormat = SimpleDateFormat("h a", Locale.getDefault())
    return timeFormat.format(date)
}
fun formatDayOfWeek(date: Date): String {
    val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    return dayOfWeekFormat.format(date)
}

fun getDate(unixFormat:Long):String
{
    val date= convertUnixToDate(unixFormat)
    return formatDate(date)
}
fun getDay(unixFormat:Long):String
{
    val date= convertUnixToDate(unixFormat)
    return formatDayOfWeek(date)
}
fun getTime(unixFormat:Long):String
{
    val date= convertUnixToDate(unixFormat)
    return formatTime(date)
}
fun tempFormat(temp:Double,unit:String):String
{
    val unitDegree=if (unit.equals("metric")) "°C" else "°F"
    return temp.toInt().toString()+unitDegree
}
fun getFullTempFormat(minTemp:Double,maxTemp:Double,unit:String):String
{
    val unitDegree=if (unit.equals("metric")) "°C" else if (unit.equals("imperial")) "°F" else "K"
    return maxTemp.toInt().toString()+"/"+minTemp.toInt().toString()+unitDegree
}

fun setHumidity(humidity: Long):String
{
    return humidity.toString()+"%"
}
fun setClouds(clouds: Long):String
{
    return clouds.toString()+"%"
}
fun setVisibility(visiblility: Long):String
{
    return visiblility.toString()+" m"
}
fun setPressure(pressure: Long):String
{
    return pressure.toString()+"hpa"
}
fun setWind(wind:Double,unit:String):String
{
    val unitDegree=if (unit.equals("imperial")) " mi/h" else " m/s"
    return wind.toString()+unitDegree
}