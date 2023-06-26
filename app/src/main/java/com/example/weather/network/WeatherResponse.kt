package com.example.weather.network

data class WeatherResponse (

    val lat : Double,
    val lon : Double,
    val timezone : String,
    val timezone_offset : Int,
    val current : Current,
    val hourly : List<Hourly>,
    val daily : List<Daily>,
    val alerts : List<Alerts>

)
data class Current (

    val dt : Int,
    val sunrise : Int,
    val sunset : Int,
    val temp : Double,
    val feels_like : Double,
    val pressure : Int,
    val humidity : Int,
    val dew_point : Double,
    val uvi : Int,
    val clouds : Int,
    val visibility : Int,
    val wind_speed : Double,
    val wind_deg : Int,
    val wind_gust : Double,
    val weather : List<Weather>
)
data class Hourly (

    val dt : Int,
    val temp : Double,
    val feels_like : Double,
    val pressure : Int,
    val humidity : Int,
    val dew_point : Double,
    val uvi : Int,
    val clouds : Int,
    val visibility : Int,
    val wind_speed : Double,
    val wind_deg : Int,
    val wind_gust : Double,
    val weather : List<Weather>,
    val pop : Double
)
data class Weather (

    val id : Int,
    val main : String,
    val description : String,
    val icon : String
)
data class Alerts (

    val sender_name : String,
    val event : String,
    val start : Int,
    val end : Int,
    val description : String,
    val tags : List<String>
)
data class Daily (

    val dt : Int,
    val sunrise : Int,
    val sunset : Int,
    val moonrise : Int,
    val moonset : Int,
    val moon_phase : Double,
    val temp : Temp,
    val feels_like : Feels_like,
    val pressure : Int,
    val humidity : Int,
    val dew_point : Double,
    val wind_speed : Double,
    val wind_deg : Int,
    val wind_gust : Double,
    val weather : List<Weather>,
    val clouds : Int,
    val pop : Double,
    val rain : Double,
    val uvi : Double
)
data class Temp (

    val day : Double,
    val min : Double,
    val max : Double,
    val night : Double,
    val eve : Double,
    val morn : Double
)
data class Feels_like (

    val day : Double,
    val night : Double,
    val eve : Double,
    val morn : Double
)