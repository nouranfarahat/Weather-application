package com.example.weather.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.databinding.HourlyForcastCardBinding
import com.example.weather.databinding.TempBinding
import com.example.weather.network.Hourly
import com.example.weather.utilities.Constants

class HourlyAdapter() :ListAdapter<Hourly, HourlyAdapter.ViewHolder>(ProductDiffUtil()) {
    lateinit var binding:TempBinding
    class ViewHolder(var binding: TempBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater:LayoutInflater=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding=TempBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct=getItem(position)
        holder.binding.timeTv.text=currentProduct.dt.toString()
        holder.binding.tempTv.text=currentProduct.temp.toString()
        Glide.with(holder.binding.hourWeatherIconIv.context)
            .load(Constants.ICON_URL+currentProduct.weather.get(0).icon)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.binding.hourWeatherIconIv)
//        holder.binding.cardRow.setOnClickListener{
//            //listener.onClick(currentProduct)
//            listener(currentProduct)
//        }

    }


}


class ProductDiffUtil:DiffUtil.ItemCallback<Hourly>()
{
    override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem.dt==newItem.dt
    }

    override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {

        return oldItem==newItem
    }

}

