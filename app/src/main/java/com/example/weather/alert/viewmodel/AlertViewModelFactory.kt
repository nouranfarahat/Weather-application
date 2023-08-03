package com.example.weather.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.allproducts.viewmodel.FavoriteViewModel
import com.example.weather.model.RepositoryInterface

class AlertViewModelFactory (private val repo: RepositoryInterface):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AlertViewModel::class.java))
        {
            AlertViewModel(repo) as T
        }
        else
        {
            throw IllegalStateException("ViewModel Class not found")
        }
    }
}