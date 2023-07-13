package com.example.weather.home.view

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import android.Manifest
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mvvm.database.ConcreteLocalSource
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.databinding.InitialSetupDialogBinding
import com.example.weather.model.Repository
import com.example.weather.network.WeatherClient
import com.example.weather.utilities.*
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var initialSetupDialogBinding: InitialSetupDialogBinding
    lateinit var viewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    var latitude: Double = 1.0
    var longitude: Double = 1.0
    var favLatitude: Double = 1.0
    var favLongitude: Double = 1.0
    lateinit var language: String
    lateinit var unit: String
    var firstLaunch = true
    lateinit var locationType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("TAG", "onCreate: ")
        sharedPreferences =
            requireActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
        editor = sharedPreferences.edit()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

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
        language = sharedPreferences.getString(Constants.LANGUAGE, Constants.ENGLISH).toString()
        unit = sharedPreferences.getString(Constants.TEMPERATURE_UNIT, Constants.STANDARD).toString()
        firstLaunch = sharedPreferences.getBoolean(Constants.FIRST_LAUNCH, true)
        locationType = sharedPreferences.getString(Constants.LOCATION, Constants.GPS).toString()

        //getLastLocation()
        homeViewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(), ConcreteLocalSource(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()
        //getLastLocation()


        if (firstLaunch) {
            initialSetUp()
            editor.putBoolean(Constants.FIRST_LAUNCH, false).apply()
            showInitialSetupDialog()
        } else {
            binding.progressBar.visibility = View.VISIBLE
            binding.mainTempCard.visibility = View.GONE
            binding.weatherDetailsCard.visibility = View.GONE
            binding.hourlyRv.visibility = View.GONE
            binding.daysRv.visibility = View.GONE
            if (locationType.equals(Constants.GPS)) {
                Log.i("TAG", "onViewCreated: enter location type")
                getLastLocation()
            } else {
                latitude = sharedPreferences.getFloat(Constants.LATITUDE, 0.0F).toDouble()
                longitude = sharedPreferences.getFloat(Constants.LONGITUDE, 0.0F).toDouble()

                Log.i("TAG", "onViewCreated: map lat $latitude , map long $longitude ")
                getWeatherApiResponse(latitude, longitude, language, unit)
            }
        }


    }

    override fun onResume() {
        super.onResume()

    }

    private val locationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location? = locationResult.lastLocation
            if (lastLocation != null) {
                latitude = lastLocation.latitude
            }
            if (lastLocation != null) {
                longitude = lastLocation.longitude
            }
            editor.putFloat(Constants.LATITUDE, latitude.toFloat()).apply()
            editor.putFloat(Constants.LONGITUDE, longitude.toFloat()).apply()

            getWeatherApiResponse(latitude, longitude, language, unit)

            Log.i("TAG", "Callback: lat=$latitude , long=$longitude ")

            println("cdfvgbhjgfdfvghj $longitude ,hghgyhg $latitude pn callback")


        }
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            Log.i("TAG", "getLastLocation: checkPermissions ")
            if (isLocationEnabled()) {
                Log.i("TAG", " getLastLocation: isLocationEnabled:  ")

                requestNewLocationData()

            } else {
                Toast.makeText(context, "Turn on Location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
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


            }
        } else {
            requestPermissions()
        }
    }

    /*private fun checkPermissions(): Boolean {
        Log.i("TAG", "checkPermissions: ")
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
    }*/
    private fun checkPermissions(): Boolean {
        val result = ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return result
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(0)
        Log.i("TAG", "requestNewLocationData: ")
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallBack,
            Looper.myLooper()
        )
    }

    private fun requestPermissions() {
        Log.i("TAG", "requestPermissions: ")
        //Change this from being in ActivityCompat
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), Constants.PERMISSION_ID
        )

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i("TAG", "onRequestPermissionsResult: ")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        Log.i("TAG", "isLocationEnabled: ")
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val addresses =
            context?.let { Geocoder(it, Locale.getDefault()).getFromLocation(lat, lng, 1) }

        return addresses?.get(0)?.getAddressLine(0) ?: "not found"
    }

    private fun showSnackbar(view: View, msgStr: String) {

        Snackbar.make(view, msgStr, Snackbar.ANIMATION_MODE_FADE).show()
    }

    private fun initialSetUp() {
        editor.putString(Constants.LANGUAGE, Constants.ENGLISH).apply()
        editor.putString(Constants.TEMPERATURE_UNIT, Constants.STANDARD).apply()

    }

    private fun showInitialSetupDialog() {
        binding.progressBar.visibility = View.GONE
        binding.mainTempCard.visibility = View.GONE
        binding.weatherDetailsCard.visibility = View.GONE
        binding.hourlyRv.visibility = View.GONE
        binding.daysRv.visibility = View.GONE
        val dialogView = LayoutInflater.from(context).inflate(R.layout.initial_setup_dialog, null)
        initialSetupDialogBinding = InitialSetupDialogBinding.bind(dialogView)
        initialSetupDialogBinding.locationInitialRg.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.gps_initial_rb -> {
                    //Changables.windSpeedUnit = "metric"
                    editor.putString(Constants.LOCATION, Constants.GPS).apply()
                    //call gps function
                    println("GPS")
                    Log.i("TAG", "showInitialSetupDialog: GPS")

                    Toast.makeText(context, "GPS", Toast.LENGTH_SHORT).show()
                }
                R.id.map_initial_rb -> {
                    //Changables.windSpeedUnit = "imperial"
                    editor.putString(Constants.LOCATION, Constants.MAP).apply()
                    //call map function
                    println("MAP")
                    Log.i("TAG", "showInitialSetupDialog: MAP")

                    Toast.makeText(context, "MAP", Toast.LENGTH_SHORT).show()

                }

            }
        }
        initialSetupDialogBinding.notificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor.putString(Constants.NOTIFICATION, Constants.ENABLED).apply()

                Toast.makeText(context, "Enabled", Toast.LENGTH_SHORT).show()
            } else {
                editor.putString(Constants.NOTIFICATION, Constants.DISABLED).apply()

                Toast.makeText(context, "Disabled", Toast.LENGTH_SHORT).show()
            }
        }

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initialSetupDialogBinding.okBtn.setOnClickListener {
            dialog.dismiss()
            binding.progressBar.visibility = View.VISIBLE
            locationType = sharedPreferences.getString(Constants.LOCATION, Constants.GPS).toString()
            if (locationType.equals(Constants.MAP)) {
                Toast.makeText(context, "MAP IN", Toast.LENGTH_SHORT).show()

                editor.putString(Constants.FRAGMENT_NAME, Constants.HOME_FRAGMENT).apply()

                val action = HomeFragmentDirections.actionHomeFragmentToMapsFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(context, "GPS IN", Toast.LENGTH_SHORT).show()

                getLastLocation()
            }
            Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show()
            //binding.progressBar.visibility = View.VISIBLE
            //binding.mainTempCard.visibility = View.VISIBLE
            //binding.weatherDetailsCard.visibility = View.VISIBLE
        }

        dialog.show()


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
                        viewModel.insertWeatherToRoom(result.data)
                        Log.i("TAG", "Success before: lat=$lat , long=$longt ")
                        val currentWeather = result.data.current
                        binding.weatherDetailsCard.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.mainTempCard.visibility = View.VISIBLE
                        binding.hourlyRv.visibility = View.VISIBLE
                        binding.daysRv.visibility = View.VISIBLE

                        val countryName = result.data.timezone.substringAfter('/')
                        editor.putString(Constants.TIMEZONE, countryName).apply()

                        binding.cityTv.text = getAddress(requireContext(),latitude,longitude,language).first
                        Log.i("TAG", "Translated Countryyyy: ${setTextViewValue(countryName, language)} ")

                        //binding.cityTv.text= translateText(countryName,Constants.ENGLISH,Constants.ARABIC)
                        binding.dateTv.text = getDate(currentWeather.dt)
                        binding.mainTempTv.text =
                            tempFormat(currentWeather.temp, Changables.temperatureUnit)
                        val iconStr =
                            mapIcons(currentWeather.weather.get(0).icon)
                        Glide.with(requireContext())
                            .load(iconStr)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(binding.mainIconIv)
                        binding.descriptionTv.text = currentWeather.weather.get(0).description

                        hourlyAdapter.submitList(result.data.hourly)
                        binding.hourlyRv.apply {
                            adapter = hourlyAdapter
                            setHasFixedSize(true)
                        }

                        dailyAdapter.submitList(result.data.daily)
                        binding.daysRv.apply {
                            adapter = dailyAdapter
                            setHasFixedSize(true)
                        }
                        Log.i("TAG", "Success: lat=$lat , long=$longt ")
                        binding.weatherDetailsCard.visibility = View.VISIBLE
                        binding.humidityValueTv.text = setHumidity(currentWeather.humidity)
                        binding.visibilityValueTv.text = setVisibility(
                            currentWeather.visibility,
                            binding.visibilityValueTv.context
                        )
                        binding.windValueTv.text = setWind(
                            currentWeather.wind_speed,
                            Changables.windSpeedUnit,
                            binding.windValueTv.context
                        )
                        binding.pressureValueTv.text =
                            setPressure(currentWeather.pressure, binding.pressureValueTv.context)
                        binding.cloudValueTv.text = setClouds(currentWeather.clouds)
                        binding.uvValueTv.text = currentWeather.uvi.toString()

                    }
                    is ApiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.mainTempCard.visibility = View.GONE
                        binding.weatherDetailsCard.visibility = View.GONE

                        Log.i("TAG", "Loading: lat=$lat , long=$longt ")

                    }

                    is ApiState.Failure -> {
                        binding.progressBar.visibility = View.GONE
                        binding.weatherDetailsCard.visibility = View.GONE
                        Log.i("TAG", "Failure: lat=$lat , long=$longt ${result.msg.message}")

                        Toast.makeText(context, result.msg.message, Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }


}