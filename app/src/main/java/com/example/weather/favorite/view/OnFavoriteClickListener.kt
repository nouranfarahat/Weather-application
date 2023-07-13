package com.example.weather.favorite.view

import com.example.weather.model.FavoriteWeather


interface OnFavoriteClickListener {
    fun onDeleteClick(weather: FavoriteWeather)
    fun onCardClick(weather: FavoriteWeather)
    fun onAddClick()
}