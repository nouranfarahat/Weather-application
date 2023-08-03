package com.example.weather.favorite.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mvvm.allproducts.viewmodel.HomeViewModel
import com.example.mvvm.allproducts.viewmodel.HomeViewModelFactory
import com.example.mvvm.database.ConcreteLocalSource
import com.example.weather.R
import com.example.weather.databinding.FragmentFavoriteWeatherBinding
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.databinding.InitialSetupDialogBinding
import com.example.weather.home.view.DailyAdapter
import com.example.weather.home.view.HomeFragmentArgs
import com.example.weather.home.view.HourlyAdapter
import com.example.weather.model.Repository
import com.example.weather.network.WeatherClient
import com.example.weather.utilities.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch


class FavoriteWeatherFragment : Fragment() {
    lateinit var binding: FragmentFavoriteWeatherBinding
    lateinit var viewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var windSpeed:String
    var latitude: Double = 1.0
    var longitude: Double = 1.0
    var favLatitude: Double = 1.0
    var favLongitude: Double = 1.0
    lateinit var language: String
    lateinit var unit: String
    lateinit var locationType: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE
            )
        editor = sharedPreferences.edit()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        language = sharedPreferences.getString(Constants.LANGUAGE, Constants.ENGLISH).toString()
        windSpeed = sharedPreferences.getString(Constants.WIND_SPEED_UNIT, Constants.METRIC).toString()

        unit =
            sharedPreferences.getString(Constants.TEMPERATURE_UNIT, Constants.STANDARD).toString()
        locationType = sharedPreferences.getString(Constants.LOCATION, Constants.GPS).toString()

        homeViewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(), ConcreteLocalSource(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()
        val favWeather = getArguments()?.let {
            val res = FavoriteWeatherFragmentArgs.fromBundle(it).favWeather
            if (res != null) {
                if(isConnected(requireContext()))
                {
                    getWeatherApiResponse(res.lat, res.lon, language, unit)

                }
                else{
                    viewModel.getCurrentWeather()
                    lifecycleScope.launch {
                        viewModel.weatherCurrenrt.collect { result ->
                            when (result) {
                                is ApiState.Success -> {
                                    val currentWeather = result.data.current

                                    binding.progressFavBar.visibility = View.GONE
                                    binding.mainFavTempTv.visibility = View.VISIBLE
                                    val countryName = result.data.timezone.substringAfter('/')
                                    //binding.cityFavTv.text = setTextViewValue(countryName, language)
                                    binding.cityFavTv.text = getAddress(
                                        requireActivity(),
                                        result.data.lat,
                                        result.data.lon,
                                        language
                                    ).first

                                    //binding.cityTv.text= translateText(countryName,Constants.ENGLISH,Constants.ARABIC)
                                    binding.dateFavTv.text = getDate(currentWeather.dt)
                                    binding.mainFavTempTv.text =
                                        tempFormat(currentWeather.temp, unit,language)
                                    val iconStr =
                                        mapIcons(currentWeather.weather.get(0).icon)
                                    Glide.with(binding.mainFavIconIv.context)
                                        .load(iconStr)
                                        .placeholder(R.drawable.ic_launcher_background)
                                        .error(R.drawable.ic_launcher_foreground)
                                        .into(binding.mainFavIconIv)
                                    binding.descriptionFavTv.text = currentWeather.weather.get(0).description

                                    hourlyAdapter.submitList(result.data.hourly)
                                    binding.hourlyFavRv.apply {
                                        adapter = hourlyAdapter
                                        setHasFixedSize(true)
                                    }

                                    dailyAdapter.submitList(result.data.daily)
                                    binding.daysFavRv.apply {
                                        adapter = dailyAdapter
                                        setHasFixedSize(true)
                                    }
                                    binding.weatherDetailsFavCard.visibility = View.VISIBLE
                                    binding.humidityValueFavTv.text = setHumidity(currentWeather.humidity)
                                    binding.visibilityValueFavTv.text = setVisibility(
                                        currentWeather.visibility,
                                        binding.visibilityValueFavTv.context
                                    )
                                    binding.windValueFavTv.text = setWind(
                                        currentWeather.wind_speed,
                                        windSpeed,
                                        unit,
                                        binding.windValueFavTv.context
                                    )
                                    binding.pressureValueFavTv.text =
                                        setPressure(currentWeather.pressure, binding.pressureValueFavTv.context)
                                    binding.cloudValueFavTv.text = setClouds(currentWeather.clouds)
                                    binding.uvValueFavTv.text = currentWeather.uvi.toString()

                                }
                                is ApiState.Loading -> {
                                    binding.progressFavBar.visibility = View.VISIBLE
                                    binding.mainFavTempTv.visibility = View.GONE
                                    binding.weatherDetailsFavCard.visibility = View.GONE


                                }

                                is ApiState.Failure -> {
                                    binding.progressFavBar.visibility = View.GONE
                                    binding.weatherDetailsFavCard.visibility = View.GONE

                                    Toast.makeText(context, result.msg.message, Toast.LENGTH_SHORT).show()

                                }
                            }
                        }

                    }
                }
                Toast.makeText(context, "Country ${res.country}", Toast.LENGTH_LONG).show()
                Log.i("TAG", "onViewCreated: From fav")

            } else
                Toast.makeText(context, "Not From Favorite", Toast.LENGTH_SHORT).show()
        }
    }
    fun getWeatherApiResponse(lat: Double, longt: Double, lang: String, unit: String) {
        viewModel.getLocationWeather(
            lat,
            longt,
            lang,
            unit
        )
        lifecycleScope.launch {

            Log.i("TAG", "onResume: lat=$lat , long=$longt ")

            println("cdfvgbhjgfdfvghj $longt ,hghgyhg $lat found")

            Log.i("TAG", "onResume after callback: lat=$lat , long=$longt ")
            viewModel.weatherResponse.collect { result ->
                when (result) {
                    is ApiState.Success -> {
                        Log.i("TAG", "Success before: lat=$lat , long=$longt ")
                        val currentWeather = result.data.current

                        binding.progressFavBar.visibility = View.GONE
                        binding.mainFavTempTv.visibility = View.VISIBLE
                        val countryName = result.data.timezone.substringAfter('/')
                        //binding.cityFavTv.text = setTextViewValue(countryName, language)
                        binding.cityFavTv.text = getAddress(
                            requireActivity(),
                            lat,
                            longt,
                            language
                        ).first

                        //binding.cityTv.text= translateText(countryName,Constants.ENGLISH,Constants.ARABIC)
                        binding.dateFavTv.text = getDate(currentWeather.dt)
                        binding.mainFavTempTv.text =
                            tempFormat(currentWeather.temp, unit,language)
                        val iconStr =
                            mapIcons(currentWeather.weather.get(0).icon)
                        Glide.with(binding.mainFavIconIv.context)
                            .load(iconStr)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(binding.mainFavIconIv)
                        binding.descriptionFavTv.text = currentWeather.weather.get(0).description

                        hourlyAdapter.submitList(result.data.hourly)
                        binding.hourlyFavRv.apply {
                            adapter = hourlyAdapter
                            setHasFixedSize(true)
                        }

                        dailyAdapter.submitList(result.data.daily)
                        binding.daysFavRv.apply {
                            adapter = dailyAdapter
                            setHasFixedSize(true)
                        }
                        Log.i("TAG", "Success: lat=$lat , long=$longt ")
                        binding.weatherDetailsFavCard.visibility = View.VISIBLE
                        binding.humidityValueFavTv.text = setHumidity(currentWeather.humidity)
                        binding.visibilityValueFavTv.text = setVisibility(
                            currentWeather.visibility,
                            binding.visibilityValueFavTv.context
                        )
                        binding.windValueFavTv.text = setWind(
                            currentWeather.wind_speed,
                            windSpeed,
                            unit,
                            binding.windValueFavTv.context
                        )
                        binding.pressureValueFavTv.text =
                            setPressure(currentWeather.pressure, binding.pressureValueFavTv.context)
                        binding.cloudValueFavTv.text = setClouds(currentWeather.clouds)
                        binding.uvValueFavTv.text = currentWeather.uvi.toString()

                    }
                    is ApiState.Loading -> {
                        binding.progressFavBar.visibility = View.VISIBLE
                        binding.mainFavTempTv.visibility = View.GONE
                        binding.weatherDetailsFavCard.visibility = View.GONE

                        Log.i("TAG", "Loading: lat=$lat , long=$longt ")

                    }

                    is ApiState.Failure -> {
                        binding.progressFavBar.visibility = View.GONE
                        binding.weatherDetailsFavCard.visibility = View.GONE
                        Log.i("TAG", "Failure: lat=$lat , long=$longt ${result.msg.message}")

                        Toast.makeText(context, result.msg.message, Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }


}