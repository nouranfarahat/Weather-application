package com.example.weather.alert.view

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.AlertCardBinding
import com.example.weather.favorite.view.OnFavoriteClickListener
import com.example.weather.model.AlertPojo
import com.example.weather.utilities.Constants


class AlertAdapter(var listener: OnAlertClickListener
) : ListAdapter<AlertPojo, AlertAdapter.ViewHolder>(AlertDiffUtil()) {

    class ViewHolder(var alertBinding: AlertCardBinding) :
        RecyclerView.ViewHolder(alertBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val alertItemBinding: AlertCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.alert_card, parent, false
        )
        return ViewHolder(alertItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentAlert = getItem(position)
        holder.alertBinding.alert = currentAlert
        holder.alertBinding.action = listener

    }
}

class AlertDiffUtil : DiffUtil.ItemCallback<AlertPojo>() {
    override fun areItemsTheSame(oldItem: AlertPojo, newItem: AlertPojo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AlertPojo, newItem: AlertPojo): Boolean {

        return oldItem == newItem
    }

}

