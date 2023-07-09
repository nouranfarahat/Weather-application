package com.example.weather.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.model.RepositoryInterface

class MapViewModelFactory(private val repo: RepositoryInterface):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(MapViewModel::class.java))
        {
            MapViewModel(repo) as T
        }
        else
        {
            throw IllegalStateException("ViewModel Class not found")
        }
    }
}