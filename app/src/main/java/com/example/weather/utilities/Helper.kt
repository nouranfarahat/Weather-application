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

fun getDate(unixFormat:Long):String
{
    val date= convertUnixToDate(unixFormat)
    return formatDate(date)
}