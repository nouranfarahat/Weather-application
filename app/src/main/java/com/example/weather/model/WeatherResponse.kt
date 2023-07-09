package com.example.weather.model

data class WeatherResponse (

    val lat : Double,
    val lon : Double,
    val timezone : String,
    val timezone_offset : Long,
    val current : Current,
    val hourly : List<Hourly>,
    val daily : List<Daily>,
    val alerts : List<Alerts>

)
data class Current (

    val dt : Long,
    val sunrise : Long,
    val sunset : Long,
    val temp : Double,
    val feels_like : Double,
    val pressure : Long,
    val humidity : Long,
    val dew_point : Double,
    val uvi : Double,
    val clouds : Long,
    val visibility : Long,
    val wind_speed : Double,
    val wind_deg : Long,
    val wind_gust : Double,
    val weather : List<Weather>
)
data class Hourly (

    val dt : Long,
    val temp : Double,
    val feels_like : Double,
    val pressure : Long,
    val humidity : Long,
    val dew_point : Double,
    val uvi : Double,
    val clouds : Long,
    val visibility : Long,
    val wind_speed : Double,
    val wind_deg : Long,
    val wind_gust : Double,
    val weather : List<Weather>,
    val pop : Double
)
data class Weather (

    val id : Long,
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

    val dt : Long,
    val sunrise : Long,
    val sunset : Long,
    val moonrise : Long,
    val moonset : Long,
    val moon_phase : Double,
    val temp : Temp,
    val feels_like : Feels_like,
    val pressure : Long,
    val humidity : Long,
    val dew_point : Double,
    val wind_speed : Double,
    val wind_deg : Long,
    val wind_gust : Double,
    val weather : List<Weather>,
    val clouds : Long,
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