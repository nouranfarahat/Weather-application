package com.example.weather.alert.view

import com.example.weather.model.AlertPojo

interface OnAlertClickListener {
    fun onDeleteClick(alert: AlertPojo)
    fun onAddClick()


}