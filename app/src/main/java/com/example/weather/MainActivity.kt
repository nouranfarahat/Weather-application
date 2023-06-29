package com.example.weather

import android.annotation.SuppressLint
import android.app.Fragment
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
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.home.view.HomeFragment
import com.example.weather.utilities.Constants
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var navController: NavController
    lateinit var navigationView: NavigationView
    lateinit var appBarConfiguration: AppBarConfiguration
    //lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mainBinding: ActivityMainBinding
    var lat:Double = 0.0
    var longt:Double=0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("TAG", "onCreate:Activity1 ")
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        //getLastLocation()

       // setContentView(R.layout.activity_main)
        Log.i("TAG", "onCreate:Activity ")
       // getLastLocation()
//        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
//        with (sharedPref.edit()) {
//            putFloat("LONG", longt.toFloat())
//            putFloat("LAT", lat.toFloat())
//            println("cdfvgbhjgfdfvghj $longt ,hghgyhg $lat in onAttach")
//
//            apply()
//        }
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        //getLastLocation()

        /*val bundle = Bundle()
        bundle.putDouble("LONG", longt)
        bundle.putDouble("LAT", lat)
        println("cdfvgbhjgfdfvghj $longt ,hghgyhg $lat")
        val fragment = HomeFragment()
        fragment.arguments=bundle*/
//        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
//        with (sharedPref.edit()) {
//            putFloat("LONG", longt.toFloat())
//            putFloat("LAT", lat.toFloat())
//            println("cdfvgbhjgfdfvghj $longt ,hghgyhg $lat in activity")
//
//            apply()
//        }
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout=findViewById(R.id.drawer_layout)
        navigationView=findViewById(R.id.navigator_layout)
       // val navigationView=findViewById<NavigationView>(R.id.navigator_layout)
        navController = findNavController(this, R.id.nav_host_fragment)



        appBarConfiguration=AppBarConfiguration(setOf(R.id.homeFragment,R.id.favoriteFragment,R.id.settingsFragment),drawerLayout)
        setupActionBarWithNavController(navController,drawerLayout)
        navigationView.setupWithNavController(navController)




    }

    /*override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        Log.i("TAG", "onAttachFragment: ")
        getLastLocation()
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putFloat("LONG", longt.toFloat())
            putFloat("LAT", lat.toFloat())
            println("cdfvgbhjgfdfvghj $longt ,hghgyhg $lat in onAttach")

            apply()
        }

    }*/

    override fun onSupportNavigateUp(): Boolean {
        val navController= findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)||super.onSupportNavigateUp()
    }
//    private val locationCallBack: LocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            Log.i("TAG", "CALL BACK ")
//
//            val lastLocation: Location = locationResult.lastLocation
//            lat = lastLocation.latitude
//            longt = lastLocation.longitude
//            Toast.makeText(this@MainActivity, lastLocation.toString(), Toast.LENGTH_LONG).show()
//
//            val sharedPref = this@MainActivity.getPreferences(Context.MODE_PRIVATE)
//            with (sharedPref.edit()) {
//                putFloat("LONG", longt.toFloat())
//                putFloat("LAT", lat.toFloat())
//                println("cdfvgbhjgfdfvghj $longt ,hghgyhg $lat in onAttach")
//
//                apply()
//            }
//            /*val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
//            with (sharedPref.edit()) {
//                putBoolean("my_key", true)
//                apply()
//            }*/
////            val bundle = Bundle()
////            bundle.putDouble("LONG", longt)
////            bundle.putDouble("LAT", lat)
////            println("cdfvgbhjgfdfvghj $longt ,hghgyhg $lat")
////            val fragment = HomeFragment()
////            fragment.arguments=bundle
//           // navController.navigate(R.id.homeFragment)
//            //val intent = Intent(Intent.ACTION_VIEW)
//            /*intent.data = Uri.parse(("sms:01094684227"))
//            intent.putExtra("sms_body",geoCoderTextView.text.toString())
//            startActivity(intent)*/
//
//        }
//    }

//    override fun onResume() {
//        super.onResume()
//        Log.i("TAG", "onResume: ")
//        getLastLocation()
//
//    }

    /*private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()

            } else {
                showSnackbar("Turn on Location", R.string.settings,
                    View.OnClickListener {
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package",
                            BuildConfig.APPLICATION_ID, null)
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    })

                /*Toast.makeText(this, "Turn on Location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)*/
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        val result = ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return result
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(0)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallBack, Looper.myLooper())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this@MainActivity
        ) { location ->
            Log.i("TAG", "onSuccess: ")

           // Toast.makeText(this@MainActivity, location.toString(), Toast.LENGTH_LONG).show()
        }

    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), Constants.PERMISSION_ID
        )

    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val addresses = Geocoder(this, Locale.getDefault()).getFromLocation(lat, lng, 1)

        return addresses?.get(0)?.getAddressLine(0) ?: "not found"
    }
    private fun showSnackbar(mainTextStringId: String, actionStringId: Int,
                             listener: View.OnClickListener) {

        Toast.makeText(this@MainActivity, mainTextStringId, Toast.LENGTH_LONG).show()
    }*/

}