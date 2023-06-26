package com.example.mvvm.allproducts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.RepositoryInterface
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllProductsViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private var mutableProducts = MutableLiveData<Gson>()
    val productList: LiveData<Gson>
        get() = mutableProducts

    init {
        getLocalProducts() //law m3mltsh de w kant getLocalProduct public hnadyha bs fe el activity?
    }

    private fun getLocalProducts() {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getProductsList().collect{
                mutableProducts.postValue(it)
            } //hal hwa hyrg3 lw7do ll default Dispatcher b3d ma y5ls?
        }

    }




}