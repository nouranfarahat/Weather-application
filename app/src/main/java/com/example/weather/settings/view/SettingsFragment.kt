package com.example.weather.settings.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.databinding.FragmentSettingsBinding
import com.example.weather.utilities.Changables
import com.example.weather.utilities.Constants
import com.example.weather.utilities.changeAppLanguage


class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

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
        binding.languageRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.enabled_rb -> {
                    editor.putString(Constants.LANGUAGE, Constants.ENGLISH).apply()
                    //Changables.language="en"

                }
                R.id.arabic_rb -> {
                    editor.putString(Constants.LANGUAGE, Constants.ARABIC).apply()
                    //Changables.language="ar"
                }

            }
            changeAppLanguage(
                sharedPreferences.getString(Constants.LANGUAGE, Constants.ENGLISH).toString()
            )
        }

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
        binding.windSpeedRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.meter_rb -> {
                    //Changables.windSpeedUnit = "metric"
                    editor.putString(Constants.WIND_SPEED_UNIT, Constants.METRIC).apply()

                }
                R.id.mile_rb -> {
                    //Changables.windSpeedUnit = "imperial"
                    editor.putString(Constants.WIND_SPEED_UNIT, Constants.METRIC).apply()

                }

            }
        }
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

                }

            }
        }
    }

}