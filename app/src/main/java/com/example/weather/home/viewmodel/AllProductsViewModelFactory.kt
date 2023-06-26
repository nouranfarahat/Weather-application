package com.example.mvvm.allproducts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.model.RepositoryInterface

class AllProductsViewModelFactory(private val repo: RepositoryInterface):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AllProductsViewModel::class.java))
        {
            AllProductsViewModel(repo) as T
        }
        else
        {
            throw IllegalStateException("ViewModel Class not found")
        }
    }
}