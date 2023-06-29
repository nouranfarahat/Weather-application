package com.example.weather.home.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mvvm.allproducts.viewmodel.HomeViewModel
import com.example.mvvm.allproducts.viewmodel.HomeViewModelFactory
import com.example.weather.BuildConfig
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.model.Repository
import com.example.weather.network.WeatherClient
import com.example.weather.utilities.ApiState
import com.example.weather.utilities.Constants
import com.example.weather.utilities.getDate
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var lat: Double = 1.0
    var longt: Double = 1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("TAG", "onCreate: ")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        Log.i("TAG", "onCreateView: ")

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        //longt = arguments?.getDouble("LONG")!!
        //lat = arguments?.getDouble("LAT")!!
//        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
//        longt = sharedPref.getFloat("LONG", 0.0F).toDouble()
//        lat = sharedPref.getFloat("LAT", 0.0F).toDouble()
//        println("cdfvgbhjgfdfvghj $longt ,hghgyhg $lat")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("TAG", "onViewCreated: ")
        //getLastLocation()
        homeViewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance()
            )
        )
        viewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
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
                        binding.progressBar.visibility = View.GONE
                        binding.mainTempCard.visibility = View.VISIBLE
                        binding.cityTv.text = result.data.timezone
                        binding.dateTv.text= getDate(result.data.current.dt)
                        val iconStr=Constants.ICON_URL+result.data.current.weather.get(0).icon+".png"
                        Glide.with(binding.mainIconIv.context)
                            .load(iconStr)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(binding.mainIconIv)
                        binding.descriptionTv.text=result.data.current.weather.get(0).description
                        Log.i("TAG", "Success: lat=$lat , long=$longt ")


                    }
                    is ApiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.mainTempCard.visibility = View.GONE
                        Log.i("TAG", "Loading: lat=$lat , long=$longt ")

                    }

                    is ApiState.Failure -> {
                        binding.progressBar.visibility = View.GONE
                        Log.i("TAG", "Failure: lat=$lat , long=$longt ${result.msg.message}")

                        Toast.makeText(context, result.msg.message, Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

    }

//    override fun onResume() {
//        super.onResume()
//        Log.i("TAG", "onResume: fragment ")
////        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
////        longt = sharedPref.getFloat("LONG", 0.0F).toDouble()
////        lat = sharedPref.getFloat("LAT", 0.0F).toDouble()
////        println("cdfvgbhjgfdfvghj $longt ,hghgyhg $lat")
//        lifecycleScope.launch {
////            val longt = arguments?.getDouble("LONG")
////            val lat = arguments?.getDouble("LAT")
//            //var lat: Double = 33.44
//            //var longt: Double = -94.04
//            println("cdfvgbhjgfdfvghj $longt ,hghgyhg $lat found")
//
//            viewModel.getLocationWeather(lat, longt,"en","standard")
//            viewModel.weatherResponse.collect { result ->
//                when (result) {
//                    is ApiState.Success -> {
//                        binding.progressBar.visibility = View.GONE
//                        binding.mainTempCard.visibility = View.VISIBLE
//                        binding.cityTv.text = result.data.timezone
//
//                    }
//                    is ApiState.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
//                        //binding.mainTempCard.visibility = View.GONE
//                    }
//
//                    else -> {
//                        binding.progressBar.visibility = View.GONE
//                        Toast.makeText(context, "Check your Connection", Toast.LENGTH_SHORT).show()
//
//                    }
//                }
//            }
//        }
//
//
//    }

    private val locationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            lat = lastLocation.latitude
            longt = lastLocation.longitude
            viewModel.getLocationWeather(lat, longt)

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
    private fun showSnackbar(mainTextStringId: String, actionStringId: Int,
                             listener: View.OnClickListener) {

        Toast.makeText(context, mainTextStringId, Toast.LENGTH_LONG).show()
    }



}