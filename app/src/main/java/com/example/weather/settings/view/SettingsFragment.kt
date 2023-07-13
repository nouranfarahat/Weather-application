package com.example.weather.settings.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.weather.R
import com.example.weather.databinding.FragmentSettingsBinding
import com.example.weather.utilities.Constants
import com.example.weather.utilities.changeAppLanguage


class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var language: String
    lateinit var unit: String
    lateinit var windSpeed:String
    lateinit var notification:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            Constants.SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        language = sharedPreferences.getString(Constants.LANGUAGE, Constants.ENGLISH).toString()
        windSpeed = sharedPreferences.getString(Constants.WIND_SPEED_UNIT, Constants.METRIC).toString()
        notification = sharedPreferences.getString(Constants.NOTIFICATION, Constants.ENABLED).toString()

        unit =
            sharedPreferences.getString(Constants.TEMPERATURE_UNIT, Constants.STANDARD).toString()
        initialSetting()
        binding.arabicRb.setOnClickListener{
            editor.putString(Constants.LANGUAGE, Constants.ARABIC).apply()
//            setLanguage(requireContext(),
//                sharedPreferences.getString(Constants.LANGUAGE, Constants.ARABIC).toString()
//            )
            changeAppLanguage(
                sharedPreferences.getString(Constants.LANGUAGE, Constants.ARABIC).toString()
            )
            //requireActivity().recreate()
        }
        binding.englishRb.setOnClickListener{
            editor.putString(Constants.LANGUAGE, Constants.ENGLISH).apply()
//            setLanguage(requireContext(),
//                sharedPreferences.getString(Constants.LANGUAGE, Constants.ENGLISH).toString()
//            )
            changeAppLanguage(
                sharedPreferences.getString(Constants.LANGUAGE, Constants.ARABIC).toString()
            )
            //requireActivity().recreate()

        }
        binding.meterRb.setOnClickListener {
            editor.putString(Constants.WIND_SPEED_UNIT, Constants.METRIC).apply()

        }
        binding.mileRb.setOnClickListener {
            editor.putString(Constants.WIND_SPEED_UNIT, Constants.IMPERIAL).apply()

        }
        /*binding.languageRg.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.arabic_rb -> {
                    editor.putString(Constants.LANGUAGE, Constants.ARABIC).apply()
                    //Changables.language="ar"
                }
                R.id.english_rb -> {
                    editor.putString(Constants.LANGUAGE, Constants.ENGLISH).apply()
                    //Changables.language="en"

                }


            }
            changeAppLanguage(
                sharedPreferences.getString(Constants.LANGUAGE, Constants.ARABIC).toString()
            )
        }*/

        binding.temperatureRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.celsius_rb -> {
                    //Changables.temperatureUnit = "metric"
                    editor.putString(Constants.TEMPERATURE_UNIT, Constants.METRIC).apply()

                }
                R.id.fehrinheit_rb -> {
                    //Changables.temperatureUnit = "imperial"
                    editor.putString(Constants.TEMPERATURE_UNIT, Constants.IMPERIAL).apply()

                }
                R.id.kelvin_rb -> {
                    //Changables.temperatureUnit = "standard"
                    editor.putString(Constants.TEMPERATURE_UNIT, Constants.STANDARD).apply()

                }

            }
        }
        /*binding.windSpeedRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.meter_rb -> {
                    //Changables.windSpeedUnit = "metric"
                    editor.putString(Constants.WIND_SPEED_UNIT, Constants.METRIC).apply()

                }
                R.id.mile_rb -> {
                    //Changables.windSpeedUnit = "imperial"
                    editor.putString(Constants.WIND_SPEED_UNIT, Constants.IMPERIAL).apply()

                }

            }
        }*/
        binding.notificationRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.enabled_rb -> {
                    //Changables.windSpeedUnit = "metric"
                    editor.putString(Constants.NOTIFICATION, Constants.ENABLED).apply()

                }
                R.id.disabled_rb -> {
                    //Changables.windSpeedUnit = "imperial"
                    editor.putString(Constants.NOTIFICATION, Constants.DISABLED).apply()

                }

            }
        }
        binding.locationRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.gps_rb -> {
                    //Changables.windSpeedUnit = "metric"
                    editor.putString(Constants.LOCATION, Constants.GPS).apply()


                }
                R.id.map_rb -> {
                    //Changables.windSpeedUnit = "imperial"
                    editor.putString(Constants.LOCATION, Constants.MAP).apply()
                    editor.putString(Constants.FRAGMENT_NAME, Constants.SETTINGS_FRAGMENT).apply()

                    val action = SettingsFragmentDirections.actionSettingsFragmentToMapsFragment()
                    findNavController().navigate(action)

                }

            }
        }
    }
    fun initialSetting()
    {
        when(language)
        {
            Constants.ARABIC->binding.arabicRb.isChecked=true
            Constants.ENGLISH->binding.englishRb.isChecked=true
        }
        when(unit)
        {
            Constants.STANDARD->binding.kelvinRb.isChecked=true
            Constants.IMPERIAL->binding.fehrinheitRb.isChecked=true
            Constants.METRIC->binding.celsiusRb.isChecked=true

        }
        when(notification)
        {
            Constants.ENABLED->binding.enabledRb.isChecked=true
            Constants.DISABLED->binding.disabledRb.isChecked=true
        }
        when(windSpeed)
        {
            Constants.METRIC->binding.meterRb.isChecked=true
            Constants.IMPERIAL->binding.mileRb.isChecked=true
        }

    }

}


