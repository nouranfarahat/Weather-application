package com.example.weather.settings.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.databinding.FragmentSettingsBinding
import com.example.weather.utilities.Changables
import com.example.weather.utilities.changeAppLanguage


class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding

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
                    Changables.language="en"

                }
                R.id.arabic_rb -> {
                    Changables.language="ar"
                }

            }
            changeAppLanguage(Changables.language)
        }

        binding.temperatureRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.celsius_rb -> {
                    Changables.temperatureUnit="metric"
                }
                R.id.fehrinheit_rb -> {
                    Changables.temperatureUnit="imperial"
                }
                R.id.kelvin_rb -> {
                    Changables.temperatureUnit="standard"
                }

            }
        }
        binding.windSpeedRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.meter_rb -> {
                    Changables.windSpeedUnit="metric"
                }
                R.id.mile_rb -> {
                    Changables.windSpeedUnit="imperial"
                }

            }
        }
    }

}