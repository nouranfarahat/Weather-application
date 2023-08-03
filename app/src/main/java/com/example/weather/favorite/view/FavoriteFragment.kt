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
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mvvm.allproducts.viewmodel.FavoriteViewModel
import com.example.mvvm.allproducts.viewmodel.FavoriteViewModelFactory
import com.example.mvvm.allproducts.viewmodel.HomeViewModel
import com.example.mvvm.allproducts.viewmodel.HomeViewModelFactory
import com.example.mvvm.database.ConcreteLocalSource
import com.example.weather.R
import com.example.weather.databinding.FragmentFavoriteBinding
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.databinding.InitialSetupDialogBinding
import com.example.weather.home.view.HomeFragmentDirections
import com.example.weather.model.FavoriteWeather
import com.example.weather.model.Repository
import com.example.weather.network.WeatherClient
import com.example.weather.utilities.Constants
import com.example.weather.utilities.FavState
import com.example.weather.utilities.isConnected
import com.example.weather.utilities.showSnackBar
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment(), OnFavoriteClickListener {

    lateinit var binding: FragmentFavoriteBinding
    lateinit var viewModel: FavoriteViewModel
    lateinit var favViewModelFactory: FavoriteViewModelFactory
    lateinit var favAdapter: FavoriteAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE
            )
        editor = sharedPreferences.edit()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        binding.lifecycleOwner = this
        binding.action = this
        favAdapter = FavoriteAdapter(this)
        binding.adapter = favAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favViewModelFactory = FavoriteViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(), ConcreteLocalSource(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, favViewModelFactory).get(FavoriteViewModel::class.java)
        lifecycleScope.launch {
            viewModel.weatherFavResponse.collect { result ->
                when (result) {
                    is FavState.Success -> {
                        binding.favoriteEmpty.visibility = View.GONE
                        binding.favRv.visibility = View.VISIBLE
                        favAdapter.submitList(result.data)
                        Log.i("TAG", "onViewCreated: Fav RV")
                    }
                    is FavState.Loading -> {
                        binding.favRv.visibility = View.GONE
                    }

                    else -> {
                        Toast.makeText(context, "Check your Connection", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }

    override fun onDeleteClick(weather: FavoriteWeather) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete")
            .setIcon(R.drawable.ic_baseline_delete_24)
            .setMessage("Are you sure delete this Country?")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteWeatherFromFav(weather)
                Toast.makeText(requireContext(), "Deleted this Country", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onCardClick(weather: FavoriteWeather) {
        //editor.putFloat(Constants.FAV_LATITUDE, weather.lat.toFloat()).apply()
        //editor.putFloat(Constants.FAV_LONGITUDE, weather.lon.toFloat()).apply()

        editor.putString(Constants.FAVORITE_FRAGMENT, Constants.FAVORITE_FRAGMENT).apply()
        //Toast.makeText(context, "To Home", Toast.LENGTH_LONG).show()
        val action =
            FavoriteFragmentDirections.actionFavoriteFragmentToFavoriteWeatherFragment(weather)
        findNavController().navigate(action)

    }

    override fun onAddClick() {
        if (isConnected(requireContext())) {
            editor.putString(Constants.FRAGMENT_NAME, Constants.FAVORITE_FRAGMENT).apply()
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToMapsFragment()
            findNavController().navigate(action)
        } else
            showSnackBar(binding.root, resources.getString(R.string.check_connection))
    }


}