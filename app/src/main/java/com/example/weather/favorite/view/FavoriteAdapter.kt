package com.example.weather.favorite.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FavoriteCardBinding
import com.example.weather.model.FavoriteWeather

class FavoriteAdapter(var listener: OnFavoriteClickListener

)
    : ListAdapter<FavoriteWeather, FavoriteAdapter.ViewHolder>(FavProductDiffUtil()) {

    class ViewHolder(var favBinding: FavoriteCardBinding): RecyclerView.ViewHolder(favBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val favProductBinding: FavoriteCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.favorite_card,parent,false)
        return ViewHolder(favProductBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentProduct=getItem(position)
        holder.favBinding.weather=currentProduct
        holder.favBinding.action=listener

    }
}

class FavProductDiffUtil: DiffUtil.ItemCallback<FavoriteWeather>()
{
    override fun areItemsTheSame(oldItem: FavoriteWeather, newItem: FavoriteWeather): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: FavoriteWeather, newItem: FavoriteWeather): Boolean {

        return oldItem==newItem
    }

}

