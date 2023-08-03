package com.example.weather.home.viewmodel

import app.cash.turbine.test
import com.example.mvvm.allproducts.viewmodel.FavoriteViewModel
import com.example.mvvm.allproducts.viewmodel.HomeViewModel
import com.example.weather.model.*
import com.example.weather.utilities.ApiState
import com.example.weather.utilities.FavState
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.core.Is
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest {
    lateinit var viewModel: HomeViewModel
    lateinit var repository: FakeRepository
    private lateinit var remoteDataSource: FakeRemoteSource
    private lateinit var localDataSource: FakeLocalSource

    private val weather1 = WeatherResponse(
        1, 1.5, 2.5, "Cairo", 4, Current(
            1111, 1111, 1111, 35.0, 4.5, 1111, 1111, 2.5, 3.5, 1111, 1111, 5.5, 1111, 4.5,
            emptyList()
        ), emptyList(), emptyList(), emptyList()
    )
    private val weather2 = WeatherResponse(
        2, 1.5, 2.5, "Berlin", 4, Current(
            22222, 22222, 22222, 35.0, 4.5, 22222, 22222, 2.5, 3.5, 22222, 22222, 5.5, 22222, 4.5,
            emptyList()
        ), emptyList(), emptyList(), emptyList()
    )
    private val weather3 = WeatherResponse(
        3, 1.5, 2.5, "Cairo", 4, Current(
            33333, 33333, 33333, 35.0, 4.5, 33333, 33333, 2.5, 3.5, 33333, 33333, 5.5, 33333, 4.5,
            emptyList()
        ), emptyList(), emptyList(), emptyList()
    )

    private val weatherList = mutableListOf<WeatherResponse>(weather1, weather2)

    @Before
    fun setUp()
    {
        //Given
        val favList= emptyList<FavoriteWeather>().toMutableList()
        val alertList= emptyList<AlertPojo>().toMutableList()

        remoteDataSource = FakeRemoteSource(weather1)
        localDataSource = FakeLocalSource(favList,alertList,weatherList)
        repository= FakeRepository(remoteDataSource,localDataSource)
        viewModel= HomeViewModel(repository)
    }


    @Test
    fun getLocationWeather_latAndLobt_countryName() = runBlockingTest{
        viewModel.getLocationWeather(1.5,2.5)
        var data=weather2
        viewModel.weatherResponse.test { this.awaitItem().apply {
            when(this)
            {
                is ApiState.Success-> {
                    data=this.data
                }
                is ApiState.Failure-> {}
                is ApiState.Loading-> {}
            }
        } }
        assertThat(data.timezone, `is` ("Cairo"))

    }

    @Test
    fun insertWeatherToRoom_return3() = runBlockingTest{
        viewModel.insertWeatherToRoom(weather3)
        var data=weather2
        viewModel.weatherResponse.test {this.awaitItem().apply {
            when(this)
            {
                is ApiState.Success-> {
                    data=this.data
                }
                is ApiState.Failure-> {}
                is ApiState.Loading-> {}
            }
        }

        }
        //Then
        assertThat(data, not(nullValue()))
    }

    @Test
    fun getCurrentWeather_returnNotNull() = runBlockingTest{
        var data:WeatherResponse = weather3
        viewModel.weatherCurrenrt.test {this.awaitItem().apply {
            when(this)
            {
                is ApiState.Success-> {
                    data=this.data
                }
                is ApiState.Failure-> {}
                is ApiState.Loading-> {}
            }
        }


        }
        //Then
        assertThat(data, not(nullValue()))
    }
}