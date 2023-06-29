package com.example.mvvm.allproducts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.RepositoryInterface
import com.example.weather.utilities.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val mutableWeather : MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val weatherResponse: StateFlow<ApiState>
        get() = mutableWeather

    /*init {
        getLocationWeather() //law m3mltsh de w kant getLocalProduct public hnadyha bs fe el activity?
    }*/
    fun getLocationWeather(latitude: Double,
                           longtude: Double,
                           language: String="en",
                           unit: String="standard")= viewModelScope.launch {

        repo.getWeatherResponse(latitude, longtude, language, unit)
            .catch {
                    e-> mutableWeather.value=ApiState.Failure(e)
                Log.i("TAG", "getLocationWeather: Catch")
            }
            .collect{
                    data-> mutableWeather.value=ApiState.Success(data)
                Log.i("TAG", "getLocationWeather: Collect")

            } //hal hwa hyrg3 lw7do ll default Dispatcher b3d ma y5ls?
    }

    /*private fun getLocalProducts() {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getProductsList()
                .catch { e-> mutableProducts.value=ApiState.Failure(e) }
                .collect{
                    data-> mutableProducts.value=ApiState.Success(data)
            } //hal hwa hyrg3 lw7do ll default Dispatcher b3d ma y5ls?
        }

    }*/



    /*fun addProductToFav(product: Product)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertProduct(product)
            getLocalProducts() ////hya de bdl ma a3ml .notifychange?
        }
    }*/


}