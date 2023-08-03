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
import com.example.weather.databinding.HourlyForcastCardBinding
import com.example.weather.databinding.TempBinding
import com.example.weather.model.Hourly
import com.example.weather.utilities.*

class HourlyAdapter() :ListAdapter<Hourly, HourlyAdapter.ViewHolder>(HourlyWeatherDiffUtil()) {
    lateinit var binding:TempBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var language:String
    lateinit var temperatureUnit:String
    class ViewHolder(var binding: TempBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater:LayoutInflater=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding=TempBinding.inflate(inflater,parent,false)
        sharedPreferences=parent.context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        language=sharedPreferences.getString(Constants.LANGUAGE,Constants.ENGLISH).toString()
        temperatureUnit=sharedPreferences.getString(Constants.TEMPERATURE_UNIT,Constants.STANDARD).toString()
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentHour=getItem(position)
        Log.i("TAG", "In adapter: ")
        /*translateString(getTime( currentHour.dt), "en", "ar") { translatedText, exception ->
            if (exception != null) {
                Log.e("Translation", "Translation failed: ${exception.message}")
            } else {
                holder.binding.timeTv.text= translatedText
                Log.d("Translation", "Translated text: $translatedText")
            }
        }*/
        holder.binding.timeTv.text= getTime(currentHour.dt)

        //holder.binding.timeTv.text= translateString(getTime( currentHour.dt),Constants.ENGLISH,Constants.ARABIC)
        holder.binding.tempTv.text= tempFormat( currentHour.temp, temperatureUnit,language)
        Glide.with(holder.binding.hourWeatherIconIv.context)
            .load(mapIcons(currentHour.weather.get(0).icon))
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.binding.hourWeatherIconIv)
//        holder.binding.cardRow.setOnClickListener{
//            //listener.onClick(currentProduct)
//            listener(currentProduct)
//        }

    }


}


class HourlyWeatherDiffUtil:DiffUtil.ItemCallback<Hourly>()
{
    override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem.dt==newItem.dt
    }

    override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {

        return oldItem==newItem
    }

}

