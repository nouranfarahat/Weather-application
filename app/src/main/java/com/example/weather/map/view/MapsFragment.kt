package com.example.weather.map.view

import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mvvm.database.ConcreteLocalSource
import com.example.weather.R
import com.example.weather.databinding.FragmentMapsBinding
import com.example.weather.map.viewmodel.MapViewModel
import com.example.weather.map.viewmodel.MapViewModelFactory
import com.example.weather.model.FavoriteWeather
import com.example.weather.model.Repository
import com.example.weather.network.WeatherClient
import com.example.weather.utilities.Constants
import com.example.weather.utilities.getAddress
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.intellij.lang.annotations.Language
import java.util.*

class MapsFragment : Fragment() {

    lateinit var binding: FragmentMapsBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var viewModel: MapViewModel
    lateinit var mapViewModelFactory: MapViewModelFactory
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    lateinit var country:String
    lateinit var city:String

    private var previousMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            Constants.SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /*val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        googleMap.setOnMapClickListener { latlng ->
            previousMarker?.remove()
            val language = sharedPreferences.getString(Constants.LANGUAGE, Constants.ENGLISH).toString()
            val location = LatLng(latlng.latitude, latlng.longitude)
            longitude = latlng.longitude
            latitude = latlng.latitude
            editor.putFloat(Constants.LATITUDE, latitude.toFloat()).apply()
            editor.putFloat(Constants.LONGITUDE, longitude.toFloat()).apply()
            //previousMarker?.remove()
            previousMarker = googleMap.addMarker(MarkerOptions().position(location))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))

            city=getAddress(requireContext(),latitude, longitude,language).first.toString()
            country= getAddress(requireContext(),latitude, longitude,language).second.toString()

            binding.mapCountry.text = country
            binding.mapCity.text = city

            //passLocation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapViewModelFactory = MapViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(), ConcreteLocalSource(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, mapViewModelFactory).get(MapViewModel::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.mapOkBtn.setOnClickListener {
            val fragmentName =
                sharedPreferences.getString(Constants.FRAGMENT_NAME, Constants.HOME_FRAGMENT)
                    .toString()

            if (fragmentName.equals(Constants.HOME_FRAGMENT)||fragmentName.equals(Constants.SETTINGS_FRAGMENT)) {
                val action = MapsFragmentDirections.actionMapsFragmentToHomeFragment()
                findNavController().navigate(action)
            }
            if (fragmentName.equals(Constants.FAVORITE_FRAGMENT)) {
                val favWeather=FavoriteWeather(lat = latitude, lon = longitude, country =city )
                viewModel.insertFavWeather(favWeather)
                val action = MapsFragmentDirections.actionMapsFragmentToFavoriteFragment()
                findNavController().navigate(action)
            }
            /*if (fragmentName.equals(Constants.ALERT_FRAGMENT)) {
                val favWeather=FavoriteWeather(lat = latitude, lon = longitude, country =city )
                viewModel.insertFavWeather(favWeather)
                val action = MapsFragmentDirections.actionMapsFragmentToFavoriteFragment()
                findNavController().navigate(action)
            }*/
        }

    }


}