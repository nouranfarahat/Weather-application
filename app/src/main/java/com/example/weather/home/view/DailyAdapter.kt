package com.example.weather.home.view

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.databinding.DayForecastCardBinding
import com.example.weather.model.Daily
import com.example.weather.utilities.*

class DailyAdapter() : ListAdapter<Daily, DailyAdapter.ViewHolder>(DailyWeatherDiffUtil()) {
    lateinit var binding: DayForecastCardBinding
    lateinit var language:String
    lateinit var temperatureUnit:String

    class ViewHolder(var binding: DayForecastCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DayForecastCardBinding.inflate(inflater, parent, false)
        val sharedPreferences: SharedPreferences=parent.context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        language=sharedPreferences.getString(Constants.LANGUAGE,Constants.ENGLISH).toString()
        temperatureUnit=sharedPreferences.getString(Constants.TEMPERATURE_UNIT,Constants.STANDARD).toString()
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDay = getItem(position)
        Log.i("TAG", "In adapter: ")
        /*translateString(getDay( currentDay.dt), "en", "ar") { translatedText, exception ->
            if (exception != null) {
                Log.e("Translation", "Translation failed: ${exception.message}")
            } else {
                holder.binding.dayTv.text= translatedText
                Log.d("Translation", "Translated text: $translatedText")
            }
        }*/
        holder.binding.dayTv.text = getDay(currentDay.dt,language)
        holder.binding.forecastTv.text = currentDay.weather.get(0).description
        holder.binding.fullDayTempTv.text =
            getFullTempFormat(currentDay.temp.min, currentDay.temp.max, temperatureUnit)
        Glide.with(holder.binding.weatherIconIv.context)
            .load(mapIcons(currentDay.weather.get(0).icon))
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.binding.weatherIconIv)
//        holder.binding.cardRow.setOnClickListener{
//            //listener.onClick(currentProduct)
//            listener(currentProduct)
//        }

    }


}


class DailyWeatherDiffUtil : DiffUtil.ItemCallback<Daily>() {
    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {

        return oldItem == newItem
    }

}

