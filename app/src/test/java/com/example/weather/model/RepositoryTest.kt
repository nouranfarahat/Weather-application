package com.example.weather.model

import com.example.weather.utilities.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RepositoryTest {
    private lateinit var remoteDataSource: FakeRemoteSource
    private lateinit var localDataSource: FakeLocalSource

    private lateinit var repo: Repository

    private val favItem1 = FavoriteWeather(1, 1.5, 2.5, "Cairo")
    private val favItem2 = FavoriteWeather(2, 2.5, 4.5, "Berlin")
    private val favItem3 = FavoriteWeather(3, 3.5, 5.5, "London")

    private val favList = mutableListOf<FavoriteWeather>(favItem1, favItem2, favItem3)

    private val alert1 =
        AlertPojo(1, 11111, 11111, 11111, 11111, "Cairo", 11111, 11111, Constants.ALARM, "fine")
    private val alert2 =
        AlertPojo(2, 22222, 22222, 22222, 22222, "Cairo", 22222, 22222, Constants.ALARM, "Bad")
    private val alert3 = AlertPojo(
        3,
        33333,
        33333,
        33333,
        33333,
        "Cairo",
        33333,
        33333,
        Constants.NOTIFICATION,
        "Sunny"
    )

    private val alertList = mutableListOf<AlertPojo>(alert1, alert2, alert3)

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

    private val weatherList = mutableListOf<WeatherResponse>(weather1, weather2, weather3)

    private val remoteTasks = weather1
    private val localTasks = listOf<WeatherResponse>(weather2, weather3)

    @Before
    fun createRepository() {
        remoteDataSource = FakeRemoteSource(remoteTasks)
        localDataSource = FakeLocalSource(favList, alertList, localTasks.toMutableList())
        repo = Repository(remoteDataSource, localDataSource)
    }

    @Test
    fun getWeatherResponse_requestWatherFromRemoteDataSource_returnData() = runBlockingTest {
        repo.getWeatherResponse(1.5, 2.5)
            .collect {
                val oneCallResponse = it
                // Then
                assertThat(oneCallResponse, IsEqual(remoteTasks))

            }

    }
    @Test
    fun getWeather_requestWatherFromLocalDataSource_returnLocalWeather() = runBlockingTest {
        //when
        repo.getWeather().collect {
            val oneCallResponse = it
            // Then
            assertThat(oneCallResponse, IsEqual(localTasks))

        }
    }



}