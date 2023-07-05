package com.example.weather.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mvvm.allproducts.viewmodel.HomeViewModel
import com.example.mvvm.allproducts.viewmodel.HomeViewModelFactory
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.model.Repository
import com.example.weather.network.WeatherClient
import com.example.weather.network.translation.TranslateClient
import com.example.weather.utilities.*
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var sharedPreferences: SharedPreferences

    var lat: Double = 1.0
    var longt: Double = 1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("TAG", "onCreate: ")
        sharedPreferences= requireActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        Log.i("TAG", "onCreateView: ")
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("TAG", "onViewCreated: ")
        //getLastLocation()
        homeViewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),TranslateClient.getInstance()
            )
        )
        viewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
        hourlyAdapter= HourlyAdapter()
        dailyAdapter= DailyAdapter()
        getLastLocation()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {

            Log.i("TAG", "onResume: lat=$lat , long=$longt ")

            println("cdfvgbhjgfdfvghj $longt ,hghgyhg $lat found")

            Log.i("TAG", "onResume after callback: lat=$lat , long=$longt ")
            viewModel.weatherResponse.collect { result ->
                when (result) {
                    is ApiState.Success -> {
                        Log.i("TAG", "Success before: lat=$lat , long=$longt ")
                        val currentWeather=result.data.current

                        binding.progressBar.visibility = View.GONE
                        binding.mainTempCard.visibility = View.VISIBLE
                        val countryName=result.data.timezone.substringAfter('/')

//                        lifecycleScope.launch {
//                            binding.cityTv.text = view?.let { translateText(it.context,countryName) }
//                        }
                        translateString(countryName, "en", "ar") { translatedText, exception ->
                            if (exception != null) {
                                Log.e("Translation", "Translation failed: ${exception.message}")
                            } else {
                                binding.cityTv.text=translatedText
                                Log.d("Translation", "Translated text: $translatedText")
                            }
                        }
                        //binding.cityTv.text= translateText(countryName,Constants.ENGLISH,Constants.ARABIC)
                        binding.dateTv.text= getDate(currentWeather.dt)
                        binding.mainTempTv.text = tempFormat(currentWeather.temp,Changables.temperatureUnit)
                        val iconStr=Constants.ICON_URL+currentWeather.weather.get(0).icon+".png"
                        Glide.with(binding.mainIconIv.context)
                            .load(iconStr)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(binding.mainIconIv)
                        binding.descriptionTv.text=currentWeather.weather.get(0).description

                        hourlyAdapter.submitList(result.data.hourly)
                        binding.hourlyRv.apply {
                            adapter=hourlyAdapter
                            setHasFixedSize(true)
                        }

                        dailyAdapter.submitList(result.data.daily)
                        binding.daysRv.apply {
                            adapter=dailyAdapter
                            setHasFixedSize(true)
                        }
                        Log.i("TAG", "Success: lat=$lat , long=$longt ")
                        binding.weatherDetailsCard.visibility=View.VISIBLE
                        binding.humidityValueTv.text= setHumidity(currentWeather.humidity)
                        binding.visibilityValueTv.text= setVisibility(currentWeather.visibility,binding.visibilityValueTv.context)
                        binding.windValueTv.text= setWind(currentWeather.wind_speed,Changables.windSpeedUnit,binding.windValueTv.context)
                        binding.pressureValueTv.text= setPressure(currentWeather.pressure,binding.pressureValueTv.context)
                        binding.cloudValueTv.text= setClouds(currentWeather.clouds)
                        binding.uvValueTv.text=currentWeather.uvi.toString()

                        //viewModel.translateText(countryName)
                        /*viewModel.translationResponse
                            .collect { data ->
                                when (data) {
                                    is TranslateState.Success -> {
                                        binding.cityTv.text = data
                                    }
                                    is TranslateState.Failure -> {
                                        Toast.makeText(context, data.msg.message, Toast.LENGTH_SHORT).show()
                                    }
                                    is TranslateState.Loading->{
                                        Log.i("TAG", "Loading:")

                                    }
                                }
                            }*/
                    }
                    is ApiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.mainTempCard.visibility = View.GONE
                        binding.weatherDetailsCard.visibility=View.GONE

                        Log.i("TAG", "Loading: lat=$lat , long=$longt ")

                    }

                    is ApiState.Failure -> {
                        binding.progressBar.visibility = View.GONE
                        binding.weatherDetailsCard.visibility=View.GONE
                        Log.i("TAG", "Failure: lat=$lat , long=$longt ${result.msg.message}")

                        Toast.makeText(context, result.msg.message, Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

    }

    private val locationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            lat = lastLocation.latitude
            longt = lastLocation.longitude
            //Constants.UnitDegree.unitDegree="standard"
            viewModel.getLocationWeather(lat, longt,Changables.language,Changables.temperatureUnit)

            Log.i("TAG", "Callback: lat=$lat , long=$longt ")

            println("cdfvgbhjgfdfvghj $longt ,hghgyhg $lat pn callback")


        }
    }
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                Log.i("TAG", "requestNewLocationData: lat=$lat , long=$longt ")

                requestNewLocationData()

            } else {
                /*showSnackbar("Turn on Location", R.string.settings,
                    View.OnClickListener {
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package",
                            BuildConfig.APPLICATION_ID, null)
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    })*/

                Toast.makeText(context, "Turn on Location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        val result = context?.let {
            ActivityCompat.checkSelfPermission(
                it, android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } == PackageManager.PERMISSION_GRANTED || context?.let {
            ActivityCompat.checkSelfPermission(
                it, android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        } == PackageManager.PERMISSION_GRANTED

        return result
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(0)
        Log.i("TAG", "requestNewLocationData: ")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallBack, Looper.myLooper())
    }

    private fun requestPermissions() {
        Log.i("TAG", "requestPermissions: ")
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), Constants.PERMISSION_ID
        )

    }
    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i("TAG", "onRequestPermissionsResult: ")
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==Constants.PERMISSION_ID)
        {
            if((grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED))
            {
                getLastLocation()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        Log.i("TAG", "isLocationEnabled: ")
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val addresses = context?.let { Geocoder(it, Locale.getDefault()).getFromLocation(lat, lng, 1) }

        return addresses?.get(0)?.getAddressLine(0) ?: "not found"
    }
    private fun showSnackbar(view: View,msgStr:String) {

        Snackbar.make(view,msgStr,Snackbar.ANIMATION_MODE_FADE).show()
    }





}