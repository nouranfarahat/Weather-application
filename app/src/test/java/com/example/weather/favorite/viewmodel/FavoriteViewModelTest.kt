package com.example.weather.favorite.viewmodel

import app.cash.turbine.test
import com.example.mvvm.allproducts.viewmodel.FavoriteViewModel
import com.example.weather.map.viewmodel.MapViewModel
import com.example.weather.model.*
import com.example.weather.utilities.FavState
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class FavoriteViewModelTest {

    lateinit var viewModel: FavoriteViewModel
    lateinit var repository: FakeRepository
    private lateinit var remoteDataSource: FakeRemoteSource
    private lateinit var localDataSource: FakeLocalSource

    private val favItem1 = FavoriteWeather(1, 1.5, 2.5, "Cairo")
    private val favItem2 = FavoriteWeather(2, 2.5, 4.5, "Berlin")
    private val favItem3 = FavoriteWeather(3, 3.5, 5.5, "London")

    private val favList = mutableListOf<FavoriteWeather>(favItem1, favItem2,favItem3)

    private val weather1 = WeatherResponse(
        1, 1.5, 2.5, "Cairo", 4, Current(
            1111, 1111, 1111, 35.0, 4.5, 1111, 1111, 2.5, 3.5, 1111, 1111, 5.5, 1111, 4.5,
            emptyList()
        ), emptyList(), emptyList(), emptyList()
    )

    @Before
    fun setUp()
    {
        //Given
        val alertList= emptyList<AlertPojo>().toMutableList()
        val weatherList= emptyList<WeatherResponse>().toMutableList()

        remoteDataSource = FakeRemoteSource(weather1)
        localDataSource = FakeLocalSource(favList,alertList,weatherList)
        repository= FakeRepository(remoteDataSource,localDataSource)
        viewModel= FavoriteViewModel(repository)
    }
    @Test
    fun getWeatherFavResponse_return3()  = runBlockingTest{
        var data:List<FavoriteWeather> = emptyList()
        viewModel.weatherFavResponse.test {this.awaitItem().apply {
            when(this)
            {
                is FavState.Success-> {
                    data=this.data
                }
                is FavState.Failure-> {}
                is FavState.Loading-> {}
            }
        }


        }
        //Then
        assertThat(data.size, Is.`is`(3))
    }

    @Test
    fun deleteWeatherFromFav_returnSize2() = runBlockingTest{
        viewModel.deleteWeatherFromFav(favItem1)
        repository.getFavWeatherList().collect{
            assertThat(it.size, Is.`is`(2))

        }
    }
}