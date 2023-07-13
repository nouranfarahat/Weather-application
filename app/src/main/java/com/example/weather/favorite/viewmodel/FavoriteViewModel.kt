package com.example.mvvm.allproducts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.FavoriteWeather
import com.example.weather.model.RepositoryInterface
import com.example.weather.utilities.ApiState
import com.example.weather.utilities.FavState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val mutableFavWeather: MutableStateFlow<FavState> = MutableStateFlow(FavState.Loading)
    val weatherFavResponse: StateFlow<FavState>
        get() = mutableFavWeather

    init {
        getLocalWeather() //law m3mltsh de w kant getLocalProduct public hnadyha bs fe el activity?
    }

    fun getLocalWeather() = viewModelScope.launch(Dispatchers.IO) {

        repo.getFavWeatherList()
            .catch { e ->
                mutableFavWeather.value = FavState.Failure(e)
                Log.i("TAG", "getLocalWeather: Fav catch")
            }
            .collect { data ->
                mutableFavWeather.value = FavState.Success(data)
                Log.i("TAG", "getLocalWeather: Fav collect")
            }

    } //hal hwa hyrg3 lw7do ll default Dispatcher b3d ma y5ls?

    fun deleteWeatherFromFav(weather: FavoriteWeather) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeWeatherFromFav(weather)
            getLocalWeather() ////hya de bdl ma a3ml .notifychange?
        }
    }


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


